package playground.layout;

import java.util.Date;
import java.util.Map;

import playground.logic.ChoreEntity;

public class ChoreTo {
	
	private String 			   playground;
	private String 			   id;
	private String 			   name;
	private String 			   type;
	private String 			   creatorPlayground;
	private String 			   creatorEmail;
	
	private LocationTo 		   location;
	
	private Date 			   creationDate;
	private Date 			   expirationDate;
	
	private Map<String,Object> attributes;
	
	public ChoreTo() {
		// TODO Auto-generated constructor stub
	}

	public ChoreTo(ChoreEntity chore) {
		super();
		this.playground = chore.getPlayground();
		this.id = chore.getId();
		this.name = chore.getName();
		this.type = chore.getType();
		this.creatorPlayground = chore.getCreatorPlayground();
		this.creatorEmail = chore.getCreatorEmail();
		this.location = new LocationTo(chore.getX(), chore.getY());
		this.creationDate = chore.getCreationDate();
		this.expirationDate = chore.getExpirationDate();
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

	public LocationTo getLocation() {
		return location;
	}

	public void setLocation(LocationTo location) {
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
	
	public ChoreEntity toEntity() {
		ChoreEntity choreToReturn = new ChoreEntity();
		choreToReturn.setAttributes(attributes);
		choreToReturn.setCreatorEmail(creatorEmail);
		choreToReturn.setExpirationDate(expirationDate);
		choreToReturn.setName(name);
		choreToReturn.setCreatorPlayground(creatorPlayground);
		choreToReturn.setType(type);
		choreToReturn.setX(location.getX());
		choreToReturn.setY(location.getY());
		return choreToReturn;
	}
	
}
