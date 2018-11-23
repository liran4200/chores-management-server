package playground.logic;

public class ChoreAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -6089648253391619542L;
	
	public ChoreAlreadyExistsException(String message) {
		super(message);
	}
}
