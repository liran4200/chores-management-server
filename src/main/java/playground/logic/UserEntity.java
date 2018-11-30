package playground.logic;

import playground.layout.NewUserForm;

public class UserEntity {
	
	private static final String PLAYGROUND = "ChoresManagement";
	
	private	String	email;
	private String	playground;
	private String  userName;
	private String	avatar;
	private String	role;
	private long	points;
	private boolean isActive;
	private long 	confirmCode;
	
	public UserEntity() {
		this.points = 0;
		this.playground = PLAYGROUND;
		this.isActive = false;
	}
	
	public UserEntity(NewUserForm newUser) {
		this();
		this.email = newUser.getEmail();
		this.userName = newUser.getUserName();
		this.avatar = newUser.getAvatar();
		this.role = newUser.getRole();
	}

	public UserEntity(String email, String userName, String avatar, String role) {
		this();
		this.email = email;
		this.userName = userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		return "UserEntity [email=" + email + ", playground=" + playground + ", userName=" + userName
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + ", isActive=" + isActive
				+ ", confirmCode=" + confirmCode + "]";
	}
	
	
	
	
	
}
