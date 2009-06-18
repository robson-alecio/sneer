package sneer.foundation.lang.exceptions;

/**
 * An exception which always contains an useful help
 * message to the end-user.
 */
public class FriendlyException extends Exception {

	private final String _help;
	
	public FriendlyException(String veryHelpfulMessage) {
		super(veryHelpfulMessage);
		_help = null;
	}

	public FriendlyException(String message, String help) {
		super(message);
		_help = help;
	}

	public FriendlyException(Throwable cause, String message, String help) {
		super(message, cause);
		_help = help;
	}


	public FriendlyException(Throwable cause, String help) {
		super(cause.getMessage(), cause);
		_help = help;
	}

	/** A useful help message to the end-user.*/
	public String getHelp() {
		return _help;
	}

}
