package playground.plugins;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.layout.TOComponents.ElementTo;
import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.services.ElementsService;

@Component
public class EditChorePlugin implements Plugin {
	
	private ElementsService elements;
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	public void setElements(ElementsService elements) {
		this.elements = elements;
	}

	@Override
	public Object execute(ActivityEntity activity) throws Exception {
		if (activity.getAttributes().containsKey("chore")) {
			String jsonStringChore = this.mapper.writeValueAsString(activity.getAttributes().get("chore"));
			ElementEntity elementToEdit = createChoreElement(jsonStringChore);
			ElementEntity updatedElement = elements.updateChoreElement(elementToEdit, activity.getElementPlayground(), activity.getElementId());
			if (updatedElement != null) {
				// Set a message for this activity
				activity.setMessageAttribute("User " + activity.getPlayerEmail() + " updated chore " + updatedElement.getElementId().toString());
				return new ElementTo(updatedElement);
			}
		}
		return null;
	}
	
	/**
	 * @param elementJsonAsString
	 * @return ElementEntity
	 * @throws IOException
	 */
	private ElementEntity createChoreElement(String elementJsonAsString) throws IOException {
		try {
			ElementEntity element = this.mapper.readValue(elementJsonAsString, ElementEntity.class);
			if (element.getType().equals("chore")) {
				return element;
			}
			return null;
		} catch (IOException e) {
			throw e;
		}
	}

}
