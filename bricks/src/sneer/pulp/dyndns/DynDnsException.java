package sneer.pulp.dyndns;

public class DynDnsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DynDnsException(String message) {
		super(message);
	}

	public DynDnsException(Throwable cause) {
		super(cause);
	}
}
