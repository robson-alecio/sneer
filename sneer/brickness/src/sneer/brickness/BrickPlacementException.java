package sneer.brickness;

public class BrickPlacementException extends RuntimeException {

	public BrickPlacementException(String message, Throwable cause) {
		super(message, cause);
	}

	public BrickPlacementException(Throwable cause) {
		super(cause);
	}

	public BrickPlacementException(String message) {
		super(message);
	}
	
	

}
