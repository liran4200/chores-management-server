package playground.plugins;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.EntityComponents.UserId;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.UserNotActiveException;
import playground.logic.exceptions.UserNotFoundException;
import playground.logic.services.UserService;
import playground.utils.PlaygroundConstants;

@Component
public class MarkAsDonePlugin extends AbsChangeElementStatusPlugin {
	
	private UserService usersApi;
	
	@Autowired
	public void setUsersApi(UserService usersApi) {
		this.usersApi = usersApi;
	}

	@Override
	public Object execute(ActivityEntity activity) throws ElementNotFoundException, UserNotFoundException, UserNotActiveException  {
		// Create and update the element in DB
		String assigenTo = new UserId(activity.getPlayerPlayground(), activity.getPlayerEmail()).toString();
		ElementEntity toUpdate = changeElementStatus(activity , PlaygroundConstants.ELEMENT_CHORE_STATUS_DONE,
				assigenTo);
		// Create and update the user in DB
		UserEntity userToUpdate = this.usersApi.getCustomUser(activity.getPlayerEmail(),activity.getPlayerPlayground());
		try {
			userToUpdate.setPoints(userToUpdate.getPoints() + (Integer) Integer.valueOf((String)toUpdate.getAttributes().get(PlaygroundConstants.ELEMENT_ATTRIBUTE_SCORE)));
			this.usersApi.updateUser(activity.getPlayerEmail(), activity.getPlayerPlayground(), userToUpdate);
		} catch (NumberFormatException e) {
			//do nothing
		}
		// Set a message for this activity
		activity.setMessageAttribute("User " + activity.getPlayerEmail() + " marked chore " + toUpdate.getName() + " as done");
		return activity;
	}
}
