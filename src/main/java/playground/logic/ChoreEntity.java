package playground.logic;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ChoreEntity {
	
	private static final String PLAYGROUND = "ChoresManagement";
	private static AtomicLong ID_COUNTER = new AtomicLong(0);

	private String 			   playground;
	private String 			   id;
	private String 			   name;
	private String 			   type;
	private String 			   creatorPlayground;
	private String 			   creatorEmail;
	
	private Double			   x;
	private Double			   y;
	
	private Date 			   creationDate;
	private Date 			   expirationDate;
	
	private Map<String,Object> attributes;
	
	public ChoreEntity() {
		this.playground = PLAYGROUND;
		this.creationDate = new Date();
		this.id = Long.toString(ID_COUNTER.incrementAndGet());
	}

	public ChoreEntity(String name, String type, String creatorPlayground,
			String creatorEmail, double x, double y, Date expirationDate) {
		this();
		this.name = name;
		this.type = type;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
		this.x = x;
		this.y = y;
		this.expirationDate = expirationDate;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreatorPlayground() {
		return creatorPlayground;
	}

	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}
	
	public double calculateDistance(double x, double y) {
		return Math.sqrt((y - this.y) * (y - this.y) + (x - this.x) * (x - this.x));
	}
}
