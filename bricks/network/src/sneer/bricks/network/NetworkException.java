package sneer.bricks.network;


public class NetworkException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NetworkException(Throwable t) {
		super(t);
	}
}
