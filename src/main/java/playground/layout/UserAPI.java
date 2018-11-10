package playground.layout;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.logic.NewRoommateForm;
import playground.logic.RoommateTo;

@RestController()
public class UserAPI {
	 
	 private static final String PATH = "/playground/users";
	 private static final String PLAYGROUND_CREATOR = "Liran";
	 private static final String PLAYGROUND = "chore-management";
	 
	 private static final String USER_EMAIL = "user1@user.com";
	 private static final String CONFIRMATION_CODE = "1234";
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH+ "/login/{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo login(
			@PathVariable("playground") String playground,
			@PathVariable("email") String email) throws Exception {
			// if this user already not exists throw exception and not return toommateTo.
			validateUserExist(email);

			return new RoommateTo(email, playground, PLAYGROUND_CREATOR, "avatar1","roomate",0);
	}
	
	@RequestMapping(method=RequestMethod.GET,
					path=PATH+ "/confirm/{playground}/{email}/{code}",
					produces= MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo confirm(@PathVariable("playground")String playground,
							  @PathVariable("email")String email,
							  @PathVariable("code")String code) throws Exception {
		//if confirmation code is not valid throw exception and not return RoommateTo
		validateConfirmationCode(code);
		
		return new RoommateTo(email, playground, "LiranConfirm", "avatar2", "roommate", 0);
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path=PATH +"/{playground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateRoommate (
			@PathVariable("playground") String name,
			@PathVariable("email")String email,
			@RequestBody RoommateTo roommate) throws Exception {
		//update roommate
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path=PATH,
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo registerRoommate (@RequestBody NewRoommateForm newRoommate) {
		// register a new user 
		return new RoommateTo(newRoommate.getEmail(), PLAYGROUND, 
				newRoommate.getRoommateName(), newRoommate.getAvatar(), newRoommate.getRole(), 0);
	}
	
	/**
	 * @param email
	 * @throws Exception if user does not exist
	 */
	private void validateUserExist(String email) throws Exception {
		if (!email.equals(USER_EMAIL)) {
			throw new Exception("this email is not exist");
		}
	
	}
	
	private void validateConfirmationCode(String code) throws Exception {
		if (!code.equals(CONFIRMATION_CODE)) {
			throw new Exception("illegal confirmation code");
		}
	}
	
}
