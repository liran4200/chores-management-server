package playground.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class UserServiceStub implements UserService{
	
	private AtomicLong codeGen;
	private Map<String,UserEntity> users;
	
	@PostConstruct
	public void init() {
		//thread safe
		this.users = Collections.synchronizedMap(new HashMap<>());
		this.codeGen   = new AtomicLong(1000);
	}

	@Override
	public void cleanup() {
		this.users.clear();
	}

	@Override
	public long createUser(UserEntity user)  throws UserAlreadyExistsException{
		String key = user.getPlayground()+user.getEmail();
		if(this.users.containsKey(key))
			throw new UserAlreadyExistsException("User - "+user.getEmail()+" already exists");
		user.setConfirmCode(codeGen.getAndIncrement());
		this.users.put(key, user);
		return user.getConfirmCode();
	}

	@Override
	public UserEntity getCustomUser(String email, String playground) throws UserNotFoundException {
		String key = playground+email;
		UserEntity user = this.users.get(key);
		if(user == null)
			throw new UserNotFoundException("User - "+email+" not found");
		
		return user;
	}
	
	
	@Override
	public UserEntity getConfirmUser(String email, String playground, long code)
			throws UserNotFoundException, InValidConfirmationCodeException {
		
		UserEntity user = getCustomUser(email, playground);
		if(code != user.getConfirmCode())
			throw new InValidConfirmationCodeException("confirmation code not matching");
		user.setIsActive(true);
		return user;
	}

	@Override
	public void updateUser(String email,String playground,UserEntity user)
			throws UserNotFoundException{
		String key = playground+email;
		if(!this.users.containsKey(key))
			throw new UserNotFoundException("User not exists");
		
		UserEntity existing = this.users.get(key);
		boolean dirty = false;
		
		if( user.getUserName()!=null && 
				!user.getUserName().equals(existing.getUserName())) {
			dirty = true;
			existing.setUserName(user.getUserName());
		}
		
		if( user.getAvatar()!=null && 
				!user.getAvatar().equals(existing.getAvatar())) {
			dirty = true;
			existing.setAvatar(user.getAvatar());
		}
		
		if( user.getRole()!= null &&
				!user.getRole().equals(user.getRole())) {
			dirty = true;
			existing.setRole(user.getRole());
		}
		
		if(dirty) {
			this.users.put(key, existing);
		}
		
	}
	
	
}
