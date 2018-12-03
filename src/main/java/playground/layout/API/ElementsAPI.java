package playground.layout.API;

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

import playground.layout.ErrorMessage;
import playground.layout.TOComponents.ElementTo;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.exceptions.ElementAlreadyExistsException;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.NoSuchAttributeException;
import playground.logic.services.ElementsService;


@RestController
public class ElementsAPI {
	
	private static final String PATH = "/playground/elements";

	private ElementsService ElementsService;
	
	 
    @Autowired
	public void setElementsService(ElementsService elementsService) {
		 this.ElementsService = elementsService;
    }
	
	
	@RequestMapping(
		method=RequestMethod.POST,
		path=PATH + "/{userPlayground}/{email}",
		produces=MediaType.APPLICATION_JSON_VALUE,
		consumes=MediaType.APPLICATION_JSON_VALUE)
	public ElementTo addNewElement (@RequestBody ElementTo newElement, 
								@PathVariable ("userPlayground") String userPlayground, 
								@PathVariable ("email") String email) throws ElementAlreadyExistsException {
		ElementEntity elementEntity = this.ElementsService.createNewElement(newElement.toEntity(), userPlayground, email);
		return new ElementTo(elementEntity);
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path=PATH + "/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement (@RequestBody ElementTo newElement,
							   @PathVariable("userPlayground") String userPlayground,
							   @PathVariable("email") String email,
							   @PathVariable("playground") String playground,
							   @PathVariable("id") String id) throws ElementNotFoundException {
		this.ElementsService.updateElement(newElement.toEntity(), userPlayground, email, playground, id);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTo getElementByID (@PathVariable("userPlayground") String userPlayground,
								 	 @PathVariable("email") String email,
								 	 @PathVariable("playground") String playground,
								 	 @PathVariable("id") String id) throws ElementNotFoundException {
		return new ElementTo(this.ElementsService.getElementById(userPlayground, email, playground, id)); 
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTo[] getAllElements (@PathVariable("userPlayground") String userPlayground,
			 					   @PathVariable("email") String email,
			 					   @RequestParam(name="size", required=false, defaultValue="10") int size, 
			 					   @RequestParam(name="page", required=false, defaultValue="0") int page) {
		return this.ElementsService.getAllElements(userPlayground, email, page, size)
				.stream()
				.map(ElementTo::new)
				.collect(Collectors.toList())
				.toArray(new ElementTo[0]);	
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTo[] getAllNearElements (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("x") double x,
			 					   				@PathVariable("y") double y,
			 					   				@PathVariable("distance") double distance,
			 					   				@RequestParam(name="size", required=false, defaultValue="10") int size, 
			 					   				@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return this.ElementsService.getAllNearElements(userPlayground, email, x, y, distance, page, size)
				.stream()
				.map(ElementTo::new)
				.collect(Collectors.toList())
				.toArray(new ElementTo[0]);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path=PATH + "/{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTo[] searchElement (@PathVariable("userPlayground") String userPlayground,
			 					   				@PathVariable("email") String email,
			 					   				@PathVariable("attributeName") String attributeName,
			 					   				@PathVariable("value") String value,
			 					   				@RequestParam(name="size", required=false, defaultValue="10") int size, 
			 					   				@RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception{
		
		return this.ElementsService.searchElement(userPlayground, email, attributeName, value, page, size)
				.stream()
				.map(ElementTo::new)
				.collect(Collectors.toList())
				.toArray(new ElementTo[0]);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleSpecificException (ElementNotFoundException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleSpecificException (NoSuchAttributeException e) {
		return handleException(e);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage handleSpecificException (ElementAlreadyExistsException e) {
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



