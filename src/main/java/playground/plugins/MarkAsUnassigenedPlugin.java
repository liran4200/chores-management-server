package playground.plugins;

import org.springframework.stereotype.Component;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.services.ElementNotFoundException;
import playground.utils.PlaygroundConstants;

@Component
public class MarkAsUnassigenedPlugin extends AbsChangeElementStatusPlugin {

	@Override
	public Object execute(ActivityEntity activity) throws ElementNotFoundException {
		ElementEntity e = changeElementStatus(activity , PlaygroundConstants.ELEMENT_STATUS_CHORE_UNASSIGNED, PlaygroundConstants.ELEMENT_CHORE_ASSIGNED_TO_NONE);
		activity.setMessageAttribute("User " + activity.getPlayerEmail() + " marked chore " + e.getName() + " as unassigened");
		return activity;

	}

}
