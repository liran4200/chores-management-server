package playground.logic;

public class ChoreAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 8568225773065076428L;
	
	
	public ChoreAlreadyExistsException(String message) {
		super(message);
	}

}
