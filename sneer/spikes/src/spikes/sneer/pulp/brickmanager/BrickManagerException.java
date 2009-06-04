package spikes.sneer.pulp.brickmanager;

public class BrickManagerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BrickManagerException(String message, Throwable t) {
		super(message, t);
	}

	public BrickManagerException(String message) {
		super(message);
	}
}
