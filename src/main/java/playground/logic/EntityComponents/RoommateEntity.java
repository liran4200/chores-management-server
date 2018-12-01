package playground.logic.EntityComponents;

import playground.layout.NewRoommateForm;

public class RoommateEntity {
	
	private static final String PLAYGROUND = "ChoresManagement";
	
	private	String	email;
	private String	playground;
	private String  roommateName;
	private String	avatar;
	private String	role;
	private long	points;
	private boolean isActive;
	private long 	confirmCode;
	
	public RoommateEntity() {
		this.points = 0;
		this.playground = PLAYGROUND;
		this.isActive = false;
	}
	
	public RoommateEntity(NewRoommateForm newRoommate) {
		this();
		this.email = newRoommate.getEmail();
		this.roommateName = newRoommate.getRoommateName();
		this.avatar = newRoommate.getAvatar();
		this.role = newRoommate.getRole();
	}

	public RoommateEntity(String email, String roommateName, String avatar, String role) {
		this();
		this.email = email;
		this.roommateName = roommateName;
		this.avatar = avatar;
		this.role = role;
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
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public long getConfirmCode() {
		return confirmCode;
	}
	
	public void setConfirmCode(long confirmCode) {
		this.confirmCode = confirmCode;
	}

	@Override
	public String toString() {
		return "RoommateEntity [email=" + email + ", playground=" + playground + ", roommateName=" + roommateName
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + ", isActive=" + isActive
				+ ", confirmCode=" + confirmCode + "]";
	}
	
	
	
	
	
}
