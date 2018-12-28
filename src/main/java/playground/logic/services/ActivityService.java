package playground.logic.services;

import playground.logic.EntityComponents.ActivityEntity;

public interface ActivityService {
	
	public Object invokeActivity(String userPlayground, String emil, ActivityEntity activity) throws ActivityInvokeFailedException;
	
}
