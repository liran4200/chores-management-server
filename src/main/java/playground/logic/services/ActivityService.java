package playground.logic.services;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.exceptions.ActivityInvokeFailedException;

public interface ActivityService {
	
	public Object invokeActivity(String userPlayground, String emil, ActivityEntity activity) throws ActivityInvokeFailedException;
	
}
