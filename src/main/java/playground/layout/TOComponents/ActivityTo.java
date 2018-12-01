package playground.layout.TOComponents;

import java.util.Map;

import playground.logic.EntityComponents.ActivityEntity;


public class ActivityTo {
	
	private String				playground;
	private String 				id;
	private String 				type;
	private String 				chorePlayground;
	private String				choreId;
	private String 				roommatePlayground;
	private String 				roommateEmail;
	private Map<String, Object> attributes;
	
	public ActivityTo() {
		// TODO Auto-generated constructor stub
	}
	
	public ActivityTo(ActivityEntity activity) {
		super();
		this.playground = activity.getPlayground();
		this.id = activity.getId();
		this.type = activity.getType();
		this.chorePlayground = activity.getChorePlayground();
		this.choreId = activity.getChoreId();
		this.roommatePlayground = activity.getRoommatePlayground();
		this.roommateEmail = activity.getRoommateEmail();
		this.attributes = activity.getAttributes();
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public ActivityEntity toEntity() {
		ActivityEntity rv = new ActivityEntity();
		rv.setAttributes(attributes);
		rv.setChoreId(choreId);
		rv.setChorePlayground(chorePlayground);
		rv.setRoommateEmail(roommateEmail);
		rv.setRoommatePlayground(roommatePlayground);
		rv.setType(type);
		return rv;
	}
}