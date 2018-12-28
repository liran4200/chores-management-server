package playground.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.dal.ActivityDao;
import playground.logic.EntityComponents.ActivityEntity;

@Component
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
			.forEach(act -> activityHistoryBoard.addHistoryRecored(
					(String)act.getAttributes().get(ActivityEntity.ATTRIBUTE_MESSAGE)));
		activity.setMessageAttribute("User " + activity.getPlayerEmail() + " asked to view activity history board");
		return activityHistoryBoard;
	}

}
