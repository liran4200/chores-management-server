package playground.logic.data;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.dal.ActivityDao;
import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.EntityComponents.UserId;
import playground.logic.exceptions.ActivityInvokeFailedException;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.UserNotActiveException;
import playground.logic.exceptions.UserNotFoundException;
import playground.logic.services.ActivityService;
import playground.logic.services.ElementsService;
import playground.logic.services.UserService;

@Service
public class JpaActivityService implements ActivityService {
	
	public static final String ACTION_MARK_AS_DONE = "ACTION_MARK_AS_DONE";
	public static final String ACTION_MARK_AS_ASSIGENED = "ACTION_MARK_AS_ASSIGENED";
	public static final String ACTION_MARK_AS_UNASSIGENED = "ACTION_MARK_AS_UNASSIGENED";

	private ActivityDao activitiesDal;
	
	@Autowired
	private ElementsService elementsApi;
	@Autowired
	private UserService usersApi;

	@PostConstruct
	public void init() {
	}
	
	@Override
	@Transactional
	public Object invokeActivity(ActivityEntity activity) throws ActivityInvokeFailedException {
		String action = (String)activity.getAttributes().get(ActivityEntity.ATTRIBUTE_ACTION_TO_PREFORM);
		try {
			switch (action) {
			case ACTION_MARK_AS_DONE:
				markAsDone(activity);
				break;
			case ACTION_MARK_AS_ASSIGENED:
				markAsAssigened(activity);
				break;
			case ACTION_MARK_AS_UNASSIGENED:
				markAsUnassigened(activity);
				break;
			default:
				throw new ActivityInvokeFailedException("no such action " + action + "attribute in activity");
			}
		} catch (ElementNotFoundException e) {
			new ActivityInvokeFailedException(e.getMessage());
		} catch (UserNotFoundException e) {
			new ActivityInvokeFailedException(e.getMessage());
		} catch (UserNotActiveException e) {
			new ActivityInvokeFailedException(e.getMessage());
		}
		activitiesDal.save(activity);
		return action;
	}

	private void markAsDone(ActivityEntity activity) throws ElementNotFoundException, UserNotFoundException, UserNotActiveException {
		// Create and update the element in DB
		String assigenTo = new UserId(activity.getPlayerPlayground(), activity.getPlayerEmail()).toString();
		ElementEntity toUpdate = changeElementStatus(activity , ElementEntity.STATUS_DONE,
				assigenTo);
		// Create and update the user in DB
		UserEntity userToUpdate = usersApi.getCustomUser(activity.getPlayerEmail(),activity.getPlayerPlayground());
		userToUpdate.setPoints(userToUpdate.getPoints() + (Integer)toUpdate.getAttributes().get(ElementEntity.ATTRIBUTE_SCORE));
		usersApi.updateUser(activity.getPlayerEmail(), activity.getPlayerPlayground(), userToUpdate);
	}
	
	private void markAsAssigened(ActivityEntity activity) throws ElementNotFoundException {
		String assigenTo = new UserId(activity.getPlayerPlayground(), activity.getPlayerEmail()).toString();
		changeElementStatus(activity , ElementEntity.STATUS_ASSIGNED, assigenTo);
	}
	
	private void markAsUnassigened(ActivityEntity activity) throws ElementNotFoundException {
		changeElementStatus(activity , ElementEntity.STATUS_UNASSIGNED, ElementEntity.ASSIGNED_TO_NONE);
	}
	
	private ElementEntity changeElementStatus(ActivityEntity activity, String status, String userId) throws ElementNotFoundException {
		ElementEntity toUpdate = elementsApi.getElementById(
				activity.getPlayerPlayground(),
				activity.getPlayerEmail(),
				activity.getElementPlayground(),
				activity.getElementId()
				);
		Map<String,Object> newAttributes = toUpdate.getAttributes();
		newAttributes.put(ElementEntity.ATTRIBUTE_STATUS, status);
		newAttributes.put(ElementEntity.ATTRIBUTE_ASSIGNED_TO,userId);
		toUpdate.setAttributes(newAttributes);
		elementsApi.updateElement(toUpdate, activity.getPlayerPlayground(),
				activity.getPlayerEmail(), activity.getElementPlayground(), activity.getElementId());
		return toUpdate;
	}
}
