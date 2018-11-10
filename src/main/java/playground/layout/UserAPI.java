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
	 
	 public static final String PATH = "/playground/users";
	 public static final String PLAYGROUND_CREATOR = "Liran";
	 public static final String PLAYGROUND = "chore-management";
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH+ "/login/{playground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo login(
			@PathVariable("playground") String playground,
			@PathVariable("email") String email) throws Exception {
			//check if this user already exists.
			//if exists return RoommateTo
			//else throws Exception
			return new RoommateTo(email, playground, "Liran", "avatar1","roomate",0);
	}
	
	@RequestMapping(method=RequestMethod.GET,
					path=PATH+ "/confirm/{playground}/{email}/{code}",
					produces= MediaType.APPLICATION_JSON_VALUE)
	public RoommateTo confirm(@PathVariable("playground")String playground,
							  @PathVariable("email")String email,
							  @PathVariable("code")String code) throws Exception {
		//check confirm the given code
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
	
	
}
