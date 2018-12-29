package playground.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.layout.TOComponents.ElementTo;
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
			ElementEntity scoreBoardElement = new ElementEntity();
			scoreBoardElement.setType(PlaygroundConstants.ELEMENT_TYPE_SCORE_BOARD);
			Map<String, Object> attributes = fetchScoreBoardToAttributes();
			scoreBoardElement.setAttributes(attributes);
			scoreBoardElement.setName(PlaygroundConstants.ELEMENT_TYPE_SCORE_BOARD);
			scoreBoardElement.setX(0.0);
			scoreBoardElement.setY(0.0);
			elements.createNewElement(scoreBoardElement);
			return new ElementTo(scoreBoardElement);
		} else {
			return new ElementTo(elements.getConstantElementByType(PlaygroundConstants.ELEMENT_TYPE_SCORE_BOARD));
		}
	}
	
	private Map<String, Object> fetchScoreBoardToAttributes() {
		Map<String, Object> scoreBoard = new HashMap<>();
		List<UserEntity> allUsers = this.users.getAllUsers();
		allUsers.stream().forEach(user -> scoreBoard.put(user.getUserName(), user.getPoints()));
		
		return scoreBoard;
	}

}
