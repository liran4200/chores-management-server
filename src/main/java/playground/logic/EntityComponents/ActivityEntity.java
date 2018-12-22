package playground.logic.EntityComponents;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity(name="Activity")
@Table(name="ACTIVITY")
public class ActivityEntity {
	
	// Type Possible Values
	public static final String MARK_AS_ASSIGENED_PLUGIN = "MarkAsAssigenedPlugin";
	public static final String MARK_AS_DOEN_PLUGIN = "MarkAsDonePlugin";
	public static final String MARK_AS_UNASSIGENED_PLUGIN = "MarkAsUnassigenedPlugin";
	public static final String GET_MESSAGE_BORD_PLUGIN = "GetMessageBordPlugin";
	// Message Attribute
	public static final String ATTRIBUTE_MESSAGE = "ATTRIBUTE_MESSAGE";

	private ActivityId			idAndPlayground;
	private String 				type;
	private String 				elementPlayground;
	private String				elementId;
	private String 				playerPlayground;
	private String 				playerEmail;
	private Map<String, Object> attributes;
	
	public ActivityEntity() {
		this.idAndPlayground = new ActivityId();
	}

	public ActivityEntity(String type, String elementPlayground, String elementId,
			String playerPlayground, String playerEmail, Map<String, Object> attributes) {
		this();
		this.type = type;
		this.elementPlayground = elementPlayground;
		this.elementId = elementId;
		this.playerPlayground = playerPlayground;
		this.playerEmail = playerEmail;
		this.attributes = attributes;
	}
	
	@EmbeddedId
	public ActivityId getIdAndPlayground() {
		return this.idAndPlayground;
	}
	
	public void setIdAndPlayground(ActivityId idAndPlayground) {
		this.idAndPlayground = idAndPlayground;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElementPlayground() {
		return elementPlayground;
	}

	public void setElementPlayground(String elementPlayground) {
		this.elementPlayground = elementPlayground;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerPlayground() {
		return playerPlayground;
	}

	public void setPlayerPlayground(String playerPlayground) {
		this.playerPlayground = playerPlayground;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}
	
	public void setMessageAttribute(String m) {
		this.attributes.put(ATTRIBUTE_MESSAGE, m);
	}
	
	@Transient
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Lob
	public String getAttributesJson () {
		try {
			return new ObjectMapper().writeValueAsString(this.attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setAttributesJson (String json) {
		try {
			this.attributes = new ObjectMapper().readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ActivityEntity)) {
			return false;
		}
		ActivityEntity element = (ActivityEntity) other;
		return this.idAndPlayground.equals(element.getIdAndPlayground());
	}
}
