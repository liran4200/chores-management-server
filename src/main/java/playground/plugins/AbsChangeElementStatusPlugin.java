package playground.plugins;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.services.ElementNotFoundException;
import playground.logic.services.ElementsService;
import playground.utils.PlaygroundConstants;

public abstract class AbsChangeElementStatusPlugin implements Plugin {
	
	private ElementsService elements;

	@Autowired
	public void setElementsApi(ElementsService elements) {
		this.elements = elements;
	}
	
	abstract public Object execute(ActivityEntity command) throws Exception;
	
	protected ElementEntity changeElementStatus(ActivityEntity activity, String status, String userId) throws ElementNotFoundException {
		ElementEntity toUpdate = elements.getElementById(
				activity.getPlayerPlayground(),
				activity.getPlayerEmail(),
				activity.getElementPlayground(),
				activity.getElementId()
				);
		Map<String,Object> newAttributes = toUpdate.getAttributes();
		if (newAttributes == null) {
			newAttributes = new HashMap<String, Object>();
		}
		newAttributes.put(PlaygroundConstants.ELEMENT_CHORE_ATTRIBUTE_STATUS, status);
		newAttributes.put(PlaygroundConstants.ELEMENT_CHORE_ATTRIBUTE_ASSIGNED_TO, userId);
		toUpdate.setAttributes(newAttributes);
		
		return elements.updateChoreElement(toUpdate, activity.getElementPlayground(), activity.getElementId());
	}
}
