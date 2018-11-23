package playground.logic;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityEntity {
	private static final String PLAYGROUND = "ChoresManagement";
	private static AtomicLong ID_COUNTER = new AtomicLong(0);
	
	private String				playground;
	private String 				id;
	private String 				type;
	private String 				chorePlayground;
	private String				choreId;
	private String 				roommatePlayground;
	private String 				roommateEmail;
	private Map<String, Object> attributes;
	
	public ActivityEntity() {
		this.playground = PLAYGROUND;
		this.id = Long.toString(ID_COUNTER.incrementAndGet());
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
}
