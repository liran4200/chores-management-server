package playground.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.services.ElementsService;
import playground.logic.services.UserService;
import playground.utils.PlaygroundConstants;

@Component
public class GetScoreBoardPlugin implements Plugin {
	
	private ElementsService elements;
	private UserService	users;
	
	@Autowired
	public void setElements(ElementsService elements, UserService users) {
		this.elements = elements;
		this.users = users;
	}

	@Override
	public Object execute(ActivityEntity command) throws Exception {
		if (!elements.isElementExistsByType(PlaygroundConstants.ELEMENT_TYPE_SCORE_BOARD)) {
			ElementEntity historyBoard = new ElementEntity();
			historyBoard.setType("ScoreBoard");
			Map<String, Object> attributes = fetchScoreBoardToAttributes();
			historyBoard.setAttributes(attributes);
			elements.createNewElement(historyBoard);
			return historyBoard;
		} else {
			return elements.getConstantElementByType(PlaygroundConstants.ELEMENT_TYPE_SCORE_BOARD);
		}
	}
	
	private Map<String, Object> fetchScoreBoardToAttributes() {
		Map<String, Object> scoreBoard = new HashMap<>();
		List<UserEntity> allUsers = this.users.getAllUsers();
		allUsers.stream().forEach(user -> scoreBoard.put(user.getUserName(), user.getPoints()));
		
		return scoreBoard;
	}

}