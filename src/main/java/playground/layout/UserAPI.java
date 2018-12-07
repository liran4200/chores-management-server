package playground.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import playground.logic.InValidConfirmationCodeException;
import playground.logic.UserAlreadyExistsException;
import playground.logic.UserEntity;
import playground.logic.UserNotFoundException;
import playground.logic.UserService;
import playground.logic.data.UserNotActiveException;


/**
 * @author Liran Yehudar
 */
@RestController()
public class UserAPI {
	
	private static final String PATH = "/playground/users";	
	private UserService userService;
	
    @Autowired
	public void setUserService(UserService userService) {
		 this.userService = userService;
    }
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH+ "/login/{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserTo login(
			@PathVariable("playground") String playground,
			@PathVariable("email") String email) throws UserNotFoundException, UserNotActiveException {
		
		// if this user not exists throw exception and not return userTo.
		UserEntity user = userService.getCustomUser(email, playground);
		return new UserTo(user);
	}
	
	@RequestMapping(method=RequestMethod.GET,
					path=PATH+ "/confirm/{playground}/{email}/{code}",
					produces= MediaType.APPLICATION_JSON_VALUE)
	public UserTo confirm(@PathVariable("playground")String playground,
							  @PathVariable("email")String email,
							  @PathVariable("code")long code) 
							  throws  UserNotFoundException, InValidConfirmationCodeException{
		
		//if confirmation code is not valid or user not found throw exception 
		UserEntity user = this.userService.getConfirmUser(email, playground, code);
		return new UserTo(user);
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path=PATH +"/{playground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateRoommate (
			@PathVariable("playground") String playground,
			@PathVariable("email")String email,
			@RequestBody UserTo user) throws UserNotFoundException, UserNotActiveException {
		
		userService.updateUser(email, playground, user.toEntity());
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path=PATH,
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserTo registerUser (@RequestBody NewUserForm newUser) 
			 throws UserAlreadyExistsException{
		
		// register a new user 
		UserEntity user = new UserEntity(newUser);
		this.userService.createUser(user);
		return new UserTo(user);
	}
	
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleSpecificException (UserNotFoundException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleSpecificException (InValidConfirmationCodeException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleSpecificException (UserNotActiveException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage handleSpecificException (UserAlreadyExistsException e) {
		return handleException(e);
	}
	
	/**
	 * This method create a error message to client.
	 * @param  Exception  
	 * @return ErrorMessage which contain message to client
	 */
	private ErrorMessage handleException(Exception e) {
		String message = e.getMessage();
		if (message == null) {
			message = "There is no relevant message";
		}
		return new ErrorMessage(message);
	}
	
	
		
}
