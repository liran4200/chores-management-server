package playground.logic;


import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.services.ActivityService;

//@Service
public class ActivityServiceStub implements ActivityService {
	
	public static final String ATTRIB_SCORE = "Score";

	
	@PostConstruct
	public void init() {
		
	}
	
	@Override
	public Object invokeActivity(ActivityEntity activity) {
		return activity.getAttributes().get(ATTRIB_SCORE);
	}
	

}
