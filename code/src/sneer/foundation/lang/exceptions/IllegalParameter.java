package sneer.foundation.lang.exceptions;

/** The checked counterpart of java.lang.IllegalArgumentException
 * It is not called simply IllegalArgument because it is easy to confuse them and throw the unchecked IllegalArgumentException causing bugs.
 */
public class IllegalParameter extends FriendlyException {

	@Deprecated
	public IllegalParameter(String message) {
		super(message, "help");
	}

	private static final long serialVersionUID = 1L;

}
