package playground.layout.APIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.layout.TOComponents.ActivityTo;
import playground.logic.services.ActivityService;



@RestController
public class ActivitiesAPI {

	private static final String PATH = "playground/activities";
	
	private ActivityService activityService;
	
	 
    @Autowired
	public void setActivityService(ActivityService activityService) {
		 this.activityService = activityService;
    }
	
	@RequestMapping(
			method=RequestMethod.POST,
			path=PATH + "/{userPlayground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Object invokeActivity (@RequestBody ActivityTo activity, 
								  @PathVariable ("userPlayground") String userPlayground, 
								  @PathVariable ("email") String email) throws Exception {
		return this.activityService.invokeActivity(activity.toEntity());
	}
	
}
