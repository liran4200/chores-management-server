package playground.logic.data;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.aop.logger.MyLog;
import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.exceptions.NoSuchAttributeException;
import playground.logic.services.ActivityService;

@Service
public class JpaActivityService implements ActivityService {
	
	public static final String ATTRIB_SCORE = "Score";
	
	@PostConstruct
	public void init() {
	}
	
	@Override
	@Transactional
	@MyLog
	public Object invokeActivity(ActivityEntity activity) throws NoSuchAttributeException {
		Object toReturn = activity.getAttributes().get(ATTRIB_SCORE);
		if(toReturn == null) {
			throw new NoSuchAttributeException("no " + ATTRIB_SCORE + " attribute in activity");
		}
		return toReturn;
	}
}
