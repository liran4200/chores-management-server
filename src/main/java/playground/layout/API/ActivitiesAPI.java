package playground.layout.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.layout.TOComponents.ActivityTo;
import playground.logic.services.ActivityService;



@RestController
public class ActivitiesAPI {
	
	private ActivityService activityService;
	
	 
    @Autowired
	public void setActivityService(ActivityService activityService) {
		 this.activityService = activityService;
    }
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="playground/activities/{userPlayground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:3000")
	public Object invokeActivity (@RequestBody ActivityTo activity, 
								  @PathVariable ("userPlayground") String userPlayground, 
								  @PathVariable ("email") String email) throws Exception {
		return this.activityService.invokeActivity(userPlayground, email, activity.toEntity());
	}
	
}
