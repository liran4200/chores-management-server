package playground.logic.services;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.exceptions.NoSuchAttributeException;

public interface ActivityService {
	
	public Object invokeActivity(ActivityEntity activity) throws NoSuchAttributeException;
}
