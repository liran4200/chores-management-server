package playground.layout.TOComponents;

import playground.logic.EntityComponents.RoommateEntity;

public class RoommateTo {
	
	private	String	email;
	private String	playground;
	private String  roommateName;
	private String	avatar;
	private String	role;
	private long	points;
	private long    code;
	
	public RoommateTo() {
		// TODO Auto-generated constructor stub
	}

	public RoommateTo(RoommateEntity roommate) {
		super();
		this.email = roommate.getEmail();
		this.playground = roommate.getPlayground();
		this.roommateName = roommate.getRoommateName();
		this.avatar = roommate.getAvatar();
		this.role = roommate.getRole();
		this.points = roommate.getPoints();
		this.code = roommate.getConfirmCode();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public String getRoommateName() {
		return roommateName;
	}

	public void setRoommateName(String roommateName) {
		this.roommateName = roommateName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}
	
	public long getCode() {
		return code;
	}
	
	public void setCode(long code) {
		this.code = code;
	}
	
	/**
	 * Change RoommateTO object to RoommateEntity
	 * @return RoommateEntity
	 */
	public RoommateEntity toEntity() {
		RoommateEntity entity = new RoommateEntity();
		entity.setEmail(this.email);
		entity.setAvatar(this.avatar);
		entity.setPlayground(this.playground);
		entity.setPoints(this.points);
		entity.setRole(this.role);
		entity.setRoommateName(this.roommateName);
		return entity;
	}
	
	

}