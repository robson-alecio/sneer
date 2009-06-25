package sneer.bricks.software.bricks.statestore.impl;

public class BrickStateStoreException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BrickStateStoreException(Class<?> brick) {
		super(message(brick));
	}

	public BrickStateStoreException(Class<?> brick, Throwable unhandledThrowable) {
		super(message(brick), unhandledThrowable);
	}

	private static String message(Class<?> brick) {
		return "Exception for store or restore a brick state (" + brick.getName() + ")";
	}
}