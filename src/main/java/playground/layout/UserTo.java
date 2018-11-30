package playground.layout;

import playground.logic.UserEntity;

public class UserTo {
	
	private	String	email;
	private String	playground;
	private String  roommateName;
	private String	avatar;
	private String	role;
	private long	points;
	private long    code;
	
	public UserTo() {
		// TODO Auto-generated constructor stub
	}

	public UserTo(UserEntity roommate) {
		super();
		this.email = roommate.getEmail();
		this.playground = roommate.getPlayground();
		this.roommateName = roommate.getUserName();
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
	public UserEntity toEntity() {
		UserEntity entity = new UserEntity();
		entity.setEmail(this.email);
		entity.setAvatar(this.avatar);
		entity.setPlayground(this.playground);
		entity.setPoints(this.points);
		entity.setRole(this.role);
		entity.setUserName(this.roommateName);
		return entity;
	}
	
	

}
