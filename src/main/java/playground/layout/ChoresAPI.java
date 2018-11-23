package playground.layout;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import playground.logic.ChoreAlreadyExistsException;
import playground.logic.ChoreEntity;
import playground.logic.ChoreNotFoundException;
import playground.logic.ChoresService;
import playground.logic.NoSuchAttributeException;


@RestController
public class ChoresAPI {
	
	private static final String PATH = "/playground/chores";

	private ChoresService choresService;
	
	 
    @Autowired
	public void setChoresService(ChoresService choresService) {
		 this.choresService = choresService;
    }
	
	
	@RequestMapping(
		method=RequestMethod.POST,
		path=PATH + "/{userPlayground}/{email}",
		produces=MediaType.APPLICATION_JSON_VALUE,
		consumes=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo addNewChore (@RequestBody ChoreTo newChore, 
								@PathVariable ("userPlayground") String userPlayground, 
								@PathVariable ("email") String email) throws ChoreAlreadyExistsException {
		ChoreEntity choreEntity = this.choresService.createNewChore(newChore.toEntity(), userPlayground, email);
		return new ChoreTo(choreEntity);
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path=PATH + "/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateChore (@RequestBody ChoreTo newChore,
							   @PathVariable("userPlayground") String userPlayground,
							   @PathVariable("email") String email,
							   @PathVariable("playground") String playground,
							   @PathVariable("id") String id) throws ChoreNotFoundException {
		this.choresService.updateChore(newChore.toEntity(), userPlayground, email, playground, id);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo getChoreByID (@PathVariable("userPlayground") String userPlayground,
								 @PathVariable("email") String email,
								 @PathVariable("playground") String playground,
								 @PathVariable("id") String id) throws ChoreNotFoundException {
		return new ChoreTo(this.choresService.getChoreById(userPlayground, email, playground, id)); 
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllChores (@PathVariable("userPlayground") String userPlayground,
			 					   @PathVariable("email") String email,
			 					   @RequestParam(name="size", required=false, defaultValue="10") int size, 
			 					   @RequestParam(name="page", required=false, defaultValue="0") int page) {
		return this.choresService.getAllChores(userPlayground, email, page, size)
				.stream()
				.map(ChoreTo::new)
				.collect(Collectors.toList())
				.toArray(new ChoreTo[0]);	
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] getAllNearChores (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("x") double x,
			 					   				@PathVariable("y") double y,
			 					   				@PathVariable("distance") double distance,
			 					   				@RequestParam(name="size", required=false, defaultValue="10") int size, 
			 					   				@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return this.choresService.getAllNearChores(userPlayground, email, x, y, distance, page, size)
				.stream()
				.map(ChoreTo::new)
				.collect(Collectors.toList())
				.toArray(new ChoreTo[0]);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ChoreTo[] searchChore (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("attributeName") String attributeName,
			 					   				@PathVariable("value") String value,
			 					   				@RequestParam(name="size", required=false, defaultValue="10") int size, 
			 					   				@RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception{
		
		return this.choresService.searchChore(userPlayground, email, attributeName, value, page, size)
				.stream()
				.map(ChoreTo::new)
				.collect(Collectors.toList())
				.toArray(new ChoreTo[0]);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleSpecificException (ChoreNotFoundException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleSpecificException (NoSuchAttributeException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage handleSpecificException (ChoreAlreadyExistsException e) {
		return handleException(e);
	}
	
	/**
	 * This method create an error message to the client.
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



