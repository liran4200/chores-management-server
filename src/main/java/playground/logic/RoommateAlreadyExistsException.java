package playground.logic;

public class RoommateAlreadyExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RoommateAlreadyExistsException(String message) {
		super(message);
	}
}
