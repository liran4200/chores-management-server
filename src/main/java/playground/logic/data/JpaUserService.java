package playground.logic.data;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.aop.logger.MyLog;
import playground.dal.NumberGenerator;
import playground.dal.NumberGeneratorDao;
import playground.dal.UserDao;
import playground.logic.exceptions.*;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.EntityComponents.UserId;
import playground.logic.exceptions.UserAlreadyExistsException;
import playground.logic.exceptions.UserNotActiveException;
import playground.logic.exceptions.UserNotFoundException;
import playground.logic.services.UserService;

@Service
public class JpaUserService implements UserService{
	
	private UserDao users;
	private NumberGeneratorDao numberGenerator;
	
	@Autowired
	public JpaUserService(UserDao users, NumberGeneratorDao numberGenerator) {
		this.users = users;
		this.numberGenerator = numberGenerator;
	}
	
	@Override
	@Transactional
	@MyLog
	public void cleanup() {
		this.users.deleteAll();
	}

	@Override
	@Transactional
	@MyLog
	public long createUser(UserEntity user) throws UserAlreadyExistsException {
		UserId key = user.getUserId();
		
		if(this.users.existsById(key))
			throw new UserAlreadyExistsException("user - " + key.getEmail() + " already exists");
		
		NumberGenerator temp = this.numberGenerator.save(new NumberGenerator());
		
		long code = temp.getNextNumber();
		user.setConfirmCode(code);
		this.users.save(user);
		return code;
	}
	
	@Override
	@Transactional(readOnly=true)
	@MyLog
	public UserEntity getCustomUser(String email, String playground) throws UserNotFoundException, UserNotActiveException {
		UserId key = new UserId(email);
		
		Optional<UserEntity> op = this.users.findById(key);
		if (op.isPresent()) {
			if(op.get().getIsActive())
				return op.get();
			else
				throw new UserNotActiveException("User - " +email+ " is not active,"
						+ "please confirm your code before ");

		}
		else
			throw new UserNotFoundException("User - "+email+" not found");	
	}
	
	@Transactional
	@Override
	@MyLog
	public UserEntity getConfirmUser(String email, String playground, long code)
			throws UserNotFoundException, InValidConfirmationCodeException {
		UserId key = new UserId(email);	
		Optional<UserEntity> op = this.users.findById(key);
		UserEntity user= null;
		if (op.isPresent()) {
			user = op.get();
		}
		else
			throw new UserNotFoundException("User - "+email+" not found");
		
		if(code != user.getConfirmCode())
			throw new InValidConfirmationCodeException("confirmation code not matching");
		
		// after confirm code user is set to active
		user.setIsActive(true);
		this.users.save(user);
		return user;
	}

	@Override
	@Transactional
	@MyLog
	public void updateUser(String email, String playground, UserEntity user) throws UserNotFoundException, UserNotActiveException {
		UserId key = user.getUserId();
		if(!this.users.existsById(key))
			throw new UserNotFoundException("User not exists");
		
		UserEntity existing = getCustomUser(email, playground);
		if(!existing.getIsActive())
			throw new UserNotActiveException("User - " +email+ " is not active,"
					+ "please confirm your code before ");
			
		if( user.getUserName()!=null && 
				!user.getUserName().equals(existing.getUserName())) {
			existing.setUserName(user.getUserName());
		}
		
		if( user.getAvatar()!=null && 
				!user.getAvatar().equals(existing.getAvatar())) {
			existing.setAvatar(user.getAvatar());
		}
		
		if( user.getRole()!= null &&
				!user.getRole().equals(existing.getRole())) {
			existing.setRole(user.getRole());
		}
		
		if( user.getPoints() != existing.getPoints()) {
			existing.setPoints(user.getPoints());
		}
		
		this.users.save(existing);
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> getAllUsers() {
		return this.users.findAllByOrderByPointsDesc();
		
	}

}
