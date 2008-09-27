package sneer.kernel.container;

public class LegoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LegoException(String message) {
		super(message);
	}

	public LegoException(String message, Throwable t) {
		super(message, t);
	}


}
