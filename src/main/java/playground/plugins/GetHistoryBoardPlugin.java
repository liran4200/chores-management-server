package playground.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.dal.ActivityDao;
import playground.layout.TOComponents.ElementTo;
import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.services.ActivityService;
import playground.logic.services.ElementsService;
import playground.utils.PlaygroundConstants;

@Component
public class GetHistoryBoardPlugin implements Plugin {
	
	private ActivityDao activitiesDal;
	private ActivityService activities;
	private ElementsService elements;
	
	@Autowired
	public void setActivitiesDal(ActivityDao activitiesDal, ActivityService activities, ElementsService elements) {
		this.activitiesDal = activitiesDal;
		this.activities = activities;
		this.elements = elements;
	}

	@Override
	public Object execute(ActivityEntity activity) throws Exception {
		ElementEntity historyBoard = new ElementEntity();
		Map<String, Object> historyAttributes = fetchHistoryBoardToAttributes();
		if (!elements.isElementExistsByType(PlaygroundConstants.ELEMENT_TYPE_HISTORY_BOARD)) {
			historyBoard.setType(PlaygroundConstants.ELEMENT_TYPE_HISTORY_BOARD);
			historyBoard.setAttributes(historyAttributes);
			historyBoard.setName(PlaygroundConstants.ELEMENT_TYPE_HISTORY_BOARD);
			historyBoard.setX(0.0);
			historyBoard.setY(0.0);
			elements.createNewElement(historyBoard);
			return new ElementTo(historyBoard);
		} else {
			historyBoard = elements.getConstantElementByType(PlaygroundConstants.ELEMENT_TYPE_HISTORY_BOARD);
			historyBoard.setAttributes(historyAttributes);
			return new ElementTo(historyBoard);
		}
	}

	private Map<String, Object> fetchHistoryBoardToAttributes() {
		Map<String, Object> historyBoard = new HashMap<>();
		List<String> historyMessages = new ArrayList<>();
		
		activities.getAllActivities().stream().forEach(act -> {
			if (act.getAttributes().containsKey(PlaygroundConstants.ACTIVITY_ATTRIBUTE_MESSAGE)) {
				historyMessages.add((String)act.getAttributes().get(PlaygroundConstants.ACTIVITY_ATTRIBUTE_MESSAGE));
			}
		});
		historyBoard.put(PlaygroundConstants.ELEMENT_ATTRIBUTE_MESSAGE, historyMessages);
		return historyBoard;
	}

}
