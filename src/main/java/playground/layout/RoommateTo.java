package playground.layout;

import playground.logic.RoommateEntity;

public class RoommateTo {
	
	private	String	email;
	private String	playground;
	private String  roommateName;
	private String	avatar;
	private String	role;
	private long	points;
	
	public RoommateTo() {
		// TODO Auto-generated constructor stub
	}

	public RoommateTo(String email, String playground, String roommateName, String avatar, String role, long points) {
		super();
		this.email = email;
		this.playground = playground;
		this.roommateName = roommateName;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
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
