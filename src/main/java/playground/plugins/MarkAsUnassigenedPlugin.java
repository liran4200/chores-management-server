package playground.plugins;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.exceptions.ElementNotFoundException;

public class MarkAsUnassigenedPlugin extends AbsChangeElementStatusPlugin {

	@Override
	public Object execute(ActivityEntity activity) throws ElementNotFoundException {
		ElementEntity e = changeElementStatus(activity , ElementEntity.STATUS_UNASSIGNED, ElementEntity.ASSIGNED_TO_NONE);
		activity.setMessageAttribute("User " + activity.getPlayerEmail() + " as marked chore " + e.getName() + " as unassigened");
		return activity;

	}

}
