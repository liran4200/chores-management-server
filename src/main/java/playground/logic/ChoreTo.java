package playground.logic;

import java.util.Date;
import java.util.Map;

public class ChoreTo {
	
	private String 			   playground;
	private String 			   id;
	private String 			   name;
	private String 			   type;
	private String 			   creatorPlayground;
	private String 			   creatorEmail;
	
	private Location 		   location;
	
	private Date 			   creationDate;
	private Date 			   expirationDate;
	
	private Map<String,Object> attributes;
	
	public ChoreTo() {
		// TODO Auto-generated constructor stub
	}

	public ChoreTo(String playground, String id, String name, String type, String creatorPlayground,
			String creatorEmail, Location location, Date creationDate, Date expirationDate) {
		super();
		this.playground = playground;
		this.id = id;
		this.name = name;
		this.type = type;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
		this.location = location;
		this.creationDate = creationDate;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
	
}
