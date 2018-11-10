package playground.layout;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.logic.ChoreTo;

@RestController
public class ChoresAPI {
	
	private static final String PATH = "/playground/chores";
	private static final String ADMIN_USER = "admin1";
	private static final String ADMIN_EMAIL = "admin@adminovich.com";
	
	private static final String ROOMMATE_USER = "rommate1";
	private static final String USER_EMAIL = "rommate1@userovich.com";
	
	
	@RequestMapping(
		method=RequestMethod.POST,
		path=PATH + "/{userPlayground}/{email}",
		produces=MediaType.APPLICATION_JSON_VALUE,
		consumes=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo addNewChore (@RequestBody ChoreTo newChore, 
								@PathVariable ("userPlayground") String userPlayground, 
								@PathVariable ("email") String email) throws Exception {
		//throws exception if not admin and not returning newChore
		validateAdminUser(userPlayground, email);
		
		//TODO add new choreTO to temp chores list
		
		return newChore;
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path=PATH + "/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateChore (@RequestBody ChoreTo newChore,
							   @PathVariable("userPlayground") String userPlayground,
							   @PathVariable("email") String email,
							   @PathVariable("playground") String playground,
							   @PathVariable("id") String id) throws Exception {
		validateAdminUser(userPlayground, email);
		
		//TODO add validate chore exist
		//TODO add update logic to chore in temp chores list
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo getChoreByID (@PathVariable("userPlayground") String userPlayground,
								 @PathVariable("email") String email,
								 @PathVariable("playground") String playground,
								 @PathVariable("id") String id) throws Exception {
		//throws exception if not registered user and not returning chore
		validateRoommateUser(userPlayground, email);
				
		//return dummy choreTo with playground and id
		return new ChoreTo(playground, id, null, null, null, null, null, null, null);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllChores (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email) throws Exception {
		//throws exception if not registered user and not returning chores array
		validateRoommateUser(userPlayground, email);
				
		List<ChoreTo> chores = Arrays.asList(
				new ChoreTo("playground", "1", null, null, null, null, null, null, null),
				new ChoreTo("playground", "2", null, null, null, null, null, null, null),
				new ChoreTo("playground", "3", null, null, null, null, null, null, null)
				);
		
		return chores.toArray(new ChoreTo[0]);		
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllNearChores (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("x") double x,
			 					   				@PathVariable("y") double y,
			 					   				@PathVariable("distance") double distance) throws Exception {
		//throws exception if not registered user and not returning chores array
		validateRoommateUser(userPlayground, email);
		
		//TODO add distance measure logic
		
		List<ChoreTo> chores = Arrays.asList(
				new ChoreTo("playground", "1", null, null, null, null, null, null, null),
				new ChoreTo("playground", "2", null, null, null, null, null, null, null),
				new ChoreTo("playground", "3", null, null, null, null, null, null, null)
				);
		
		return chores.toArray(new ChoreTo[0]);		
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] searchChore (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("attributeName") String attributeName,
			 					   				@PathVariable("value") String value) throws Exception{
		//throws exception if not registered user and not returning chores array
		validateRoommateUser(userPlayground, email);
		
		//TODO add search logic
		
		List<ChoreTo> chores = Arrays.asList(
				new ChoreTo("playground", "1", null, null, null, null, null, null, null),
				new ChoreTo("playground", "2", null, null, null, null, null, null, null),
				new ChoreTo("playground", "3", null, null, null, null, null, null, null)
				);
		
		return chores.toArray(new ChoreTo[0]);		
	}

	
	//TODO move validation methods to ValidatorsUtils single tone
	
	/**
	 * @param userPlayground
	 * @param email
	 * @return true if the user is admin
	 * @throws Exception
	 */
	public void validateAdminUser(String userPlayground, String email) throws Exception {
		if (!(userPlayground.equals(ADMIN_USER) && email.equals(ADMIN_EMAIL))) {
			throw new Exception("user is not admin");
		}
	}
	
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



