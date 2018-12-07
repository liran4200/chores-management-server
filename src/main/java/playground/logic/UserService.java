package playground.logic;

import playground.logic.data.UserNotActiveException;

public interface UserService {
	
	public void cleanup();
	
	public long createUser(UserEntity user) 
			throws UserAlreadyExistsException;
	
	public UserEntity getCustomUser(String email,String playground)
			throws UserNotFoundException, UserNotActiveException;
	
	public UserEntity getConfirmUser(String email, String playground, long code)
			throws UserNotFoundException, InValidConfirmationCodeException;
	
	public void updateUser(String email, String playground, UserEntity user) 
			throws UserNotFoundException,UserNotActiveException;
	
}