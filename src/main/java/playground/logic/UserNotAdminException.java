package playground.logic;

public class UserNotAdminException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserNotAdminException(String message) {
		super(message);
	}
}
