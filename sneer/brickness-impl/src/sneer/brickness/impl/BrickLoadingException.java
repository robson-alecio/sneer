package sneer.brickness.impl;

public class BrickLoadingException extends RuntimeException {

	public BrickLoadingException(Throwable cause) {
		super(cause);
	}

	public BrickLoadingException(String message) {
		super(message);
	}

}
