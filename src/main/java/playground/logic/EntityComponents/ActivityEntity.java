package playground.logic.EntityComponents;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EmbeddedId;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ActivityEntity {	
	private ActivityId			idAndPlayground;

	private String 				type;
	private String 				chorePlayground;
	private String				choreId;
	private String 				roommatePlayground;
	private String 				roommateEmail;
	private Map<String, Object> attributes;
	
	public ActivityEntity() {
		this.idAndPlayground = new ActivityId();
	}

	public ActivityEntity(String type, String chorePlayground, String choreId,
			String roommatePlayground, String roommateEmail, Map<String, Object> attributes) {
		this();
		this.type = type;
		this.chorePlayground = chorePlayground;
		this.choreId = choreId;
		this.roommatePlayground = roommatePlayground;
		this.roommateEmail = roommateEmail;
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

	public String getChorePlayground() {
		return chorePlayground;
	}

	public void setChorePlayground(String chorePlayground) {
		this.chorePlayground = chorePlayground;
	}

	public String getChoreId() {
		return choreId;
	}

	public void setChoreId(String choreId) {
		this.choreId = choreId;
	}

	public String getRoommatePlayground() {
		return roommatePlayground;
	}

	public void setRoommatePlayground(String roommatePlayground) {
		this.roommatePlayground = roommatePlayground;
	}

	public String getRoommateEmail() {
		return roommateEmail;
	}

	public void setRoommateEmail(String roommateEmail) {
		this.roommateEmail = roommateEmail;
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
