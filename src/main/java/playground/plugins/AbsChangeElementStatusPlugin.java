package playground.plugins;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.services.ElementsService;

public abstract class AbsChangeElementStatusPlugin implements Plugin {
	
	private ElementsService elementsApi;

	@Autowired
	public void setElementsApi(ElementsService elementsApi) {
		this.elementsApi = elementsApi;
	}
	
	abstract public Object execute(ActivityEntity command) throws Exception;
	
	protected ElementEntity changeElementStatus(ActivityEntity activity, String status, String userId) throws ElementNotFoundException {
		ElementEntity toUpdate = elementsApi.getElementById(
				activity.getPlayerPlayground(),
				activity.getPlayerEmail(),
				activity.getElementPlayground(),
				activity.getElementId()
				);
		Map<String,Object> newAttributes = toUpdate.getAttributes();
		newAttributes.put(ElementEntity.ATTRIBUTE_STATUS, status);
		newAttributes.put(ElementEntity.ATTRIBUTE_ASSIGNED_TO,userId);
		toUpdate.setAttributes(newAttributes);
		elementsApi.updateElement(toUpdate, activity.getPlayerPlayground(),
				activity.getPlayerEmail(), activity.getElementPlayground(), activity.getElementId());
		return toUpdate;
	}
}
