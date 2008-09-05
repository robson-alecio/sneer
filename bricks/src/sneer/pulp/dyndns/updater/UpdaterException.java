package sneer.pulp.dyndns.updater;

public class UpdaterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UpdaterException(String message) {
		super(message);
	}

	public UpdaterException(Throwable cause) {
		super(cause);
	}
}
