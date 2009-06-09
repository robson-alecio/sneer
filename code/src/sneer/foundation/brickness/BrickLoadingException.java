package sneer.foundation.brickness;

public class BrickLoadingException extends RuntimeException {

	public BrickLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public BrickLoadingException(Throwable cause) {
		super(cause);
	}

	public BrickLoadingException(String message) {
		super(message);
	}
	
	

}
