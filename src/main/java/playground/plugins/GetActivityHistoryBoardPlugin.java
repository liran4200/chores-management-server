package playground.plugins;

import org.springframework.beans.factory.annotation.Autowired;

import playground.dal.ActivityDao;
import playground.logic.EntityComponents.ActivityEntity;

public class GetActivityHistoryBoardPlugin implements Plugin {
	
	private ActivityDao activitiesDal;
	
	@Autowired
	public void setActivitiesDal(ActivityDao activitiesDal) {
		this.activitiesDal = activitiesDal;
	}

	@Override
	public Object execute(ActivityEntity activity) throws Exception {
		ActivityHistoryBoard activityHistoryBoard = new ActivityHistoryBoard();
		this.activitiesDal.findAll()
			.forEach(activite -> activityHistoryBoard.addHistoryRecored(
					(String)activite.getAttributes().get(ActivityEntity.ATTRIBUTE_MESSAGE))
					);
		activity.setMessageAttribute("User " + activity.getPlayerEmail() + " as asked to view activity history board");
		return activityHistoryBoard;
	}

}