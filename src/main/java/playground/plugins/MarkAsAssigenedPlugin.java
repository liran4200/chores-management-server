package playground.plugins;

import org.springframework.stereotype.Component;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.UserId;

@Component
public class MarkAsAssigenedPlugin extends AbsChangeElementStatusPlugin {

	@Override
	public Object execute(ActivityEntity activity) throws Exception {
		String assigenTo = new UserId(activity.getPlayerPlayground(), activity.getPlayerEmail()).toString();
		ElementEntity e =changeElementStatus(activity , ElementEntity.STATUS_ASSIGNED, assigenTo);
		activity.setMessageAttribute("User " + activity.getPlayerEmail() + " as assigened chore " + e.getName() + " to himself");
		return activity;
	}

}
