package playground.layout;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.logic.ActivityTo;


@RestController
public class ActivitiesAPI {

	private static final String PATH = "playground/activities";
	
	private static final String ROOMMATE_USER = "rommate1";
	private static final String USER_EMAIL = "rommate1@userovich.com";
	
	private static final int ACTIVITY_SCORE = 10;
	
	
	
	@RequestMapping(
			method=RequestMethod.POST,
			path=PATH + "/{userPlayground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Object invokeActivity (@RequestBody ActivityTo activity, 
								  @PathVariable ("userPlayground") String userPlayground, 
								  @PathVariable ("email") String email) throws Exception {
		//throws exception if not registered user and not returning score
		validateRoommateUser(userPlayground, email);
			
		//returns the activity score		
		return ACTIVITY_SCORE;
	}
	
	
	//TODO move validation methods to ValidatorsUtils single tone
	/**
	 * @param userPlayground
	 * @param email
	 * @return true if the user is registered
	 * @throws Exception
	 */
	public void validateRoommateUser(String userPlayground, String email) throws Exception {
		if (!(userPlayground.equals(ROOMMATE_USER) && email.equals(USER_EMAIL))) {
			throw new Exception("user is not registered");
		}
	}
}
