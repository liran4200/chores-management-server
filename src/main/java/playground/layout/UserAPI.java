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
import playground.logic.RoommateAlreadyExistsException;
import playground.logic.RoommateEntity;
import playground.logic.RoommateNotFoundException;
import playground.logic.RoommateService;

@RestController()
public class UserAPI {
	
	private static final String PATH = "/playground/users";	
	private RoommateService roommateService;
	
	 
    @Autowired
	public void setRoommateService(RoommateService roommateService) {
		 this.roommateService = roommateService;
    }
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH+ "/login/{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo login(
			@PathVariable("playground") String playground,
			@PathVariable("email") String email) throws RoommateNotFoundException {
		// if this roommate not exists throw exception and not return roommateTo.
		RoommateEntity roommate = roommateService.getCustomRoommate(email, playground);
		return new RoommateTo(roommate);
	}
	
	@RequestMapping(method=RequestMethod.GET,
					path=PATH+ "/confirm/{playground}/{email}/{code}",
					produces= MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo confirm(@PathVariable("playground")String playground,
							  @PathVariable("email")String email,
							  @PathVariable("code")String code) 
							  throws  RoommateNotFoundException, InValidConfirmationCodeException{
		//if confirmation code is not valid or roommate not found throw exception 
		RoommateEntity roommate = this.roommateService.getConfirmRoommate(email, playground, code);
		return new RoommateTo(roommate);
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path=PATH +"/{playground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateRoommate (
			@PathVariable("playground") String playground,
			@PathVariable("email")String email,
			@RequestBody RoommateTo roommate) throws Exception {
		// should to decide later where to check playground field
		roommateService.updateRoommate(email, playground,roommate.toEntity());
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path=PATH,
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo registerRoommate (@RequestBody NewRoommateForm newRoommate) 
			 throws RoommateAlreadyExistsException{
		// register a new roommate 
		// should to decide later where to check playground field
		RoommateEntity roommate = new RoommateEntity(newRoommate);
		this.roommateService.createRoommate(roommate);
		return new RoommateTo(roommate);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleSpecificException (RoommateNotFoundException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleSpecificException (InValidConfirmationCodeException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage handleSpecificException (RoommateAlreadyExistsException e) {
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
