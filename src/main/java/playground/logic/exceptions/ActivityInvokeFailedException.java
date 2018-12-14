package playground.logic.exceptions;

public class ActivityInvokeFailedException extends RuntimeException {

	private static final long serialVersionUID = -6601777217031084339L;
	
	public ActivityInvokeFailedException(String message) {
		super(message);
	}
}
