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
	
	private static final String ADMIN_USER = "admin1";
	private static final String ADMIN_EMAIL = "admin@adminovich.com";
	
	private static final String CHORES_URL_PERFIX = "/chores";
	
	
	@RequestMapping(
		method=RequestMethod.POST,
		path="/chores/{userPlayground}/{email}",
		produces=MediaType.APPLICATION_JSON_VALUE,
		consumes=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo addNewChore (@RequestBody ChoreTo newChore, 
								@PathVariable ("userPlayground") String userPlayground, 
								@PathVariable ("email") String email) throws Exception {
		//throws exception if not admin and not returning newChore
		validateAdminUser(userPlayground, email);
		return newChore;
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path="/chores/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateChore (@RequestBody ChoreTo newChore,
							   @PathVariable("userPlayground") String userPlayground,
							   @PathVariable("email") String email,
							   @PathVariable("playground") String playground,
							   @PathVariable("id") String id) throws Exception {
		validateAdminUser(userPlayground, email);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/chores/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo getChoreByID (@PathVariable("userPlayground") String userPlayground,
								 @PathVariable("email") String email,
								 @PathVariable("playground") String playground,
								 @PathVariable("id") String id) {
		//return dummy choreTo with playground and id
		return new ChoreTo(playground, id, null, null, null, null, null, null, null, null);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/chores/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllUsersAssignedChores (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email) {
		List<ChoreTo> chores = Arrays.asList(
				new ChoreTo("playground", "1", null, null, null, null, null, null, null, null),
				new ChoreTo("playground", "2", null, null, null, null, null, null, null, null),
				new ChoreTo("playground", "3", null, null, null, null, null, null, null, null)
				);
		
		return chores.toArray(new ChoreTo[0]);		
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/chores/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllUsersAssignedChores (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("x") double x,
			 					   				@PathVariable("y") double y,
			 					   				@PathVariable("distance") double distance) {
		List<ChoreTo> chores = Arrays.asList(
				new ChoreTo("playground", "1", null, null, null, null, null, null, null, null),
				new ChoreTo("playground", "2", null, null, null, null, null, null, null, null),
				new ChoreTo("playground", "3", null, null, null, null, null, null, null, null)
				);
		
		return chores.toArray(new ChoreTo[0]);		
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/chores/{{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllUsersAssignedChores (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("attributeName") String attributeName,
			 					   				@PathVariable("value") String value) {
		List<ChoreTo> chores = Arrays.asList(
				new ChoreTo("playground", "1", null, null, null, null, null, null, null, null),
				new ChoreTo("playground", "2", null, null, null, null, null, null, null, null),
				new ChoreTo("playground", "3", null, null, null, null, null, null, null, null)
				);
		
		return chores.toArray(new ChoreTo[0]);		
	}
	
	
	public boolean validateAdminUser(String userPlayground, String email) throws Exception {
		if (userPlayground.equals(ADMIN_USER) && email.equals(ADMIN_EMAIL)) {
			return true;
		} else {
			throw new Exception("user is not admin");
		}
	}
}



