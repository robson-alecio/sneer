package sneer.foundation.lang.exceptions;

/** The checked counterpart of java.lang.IllegalArgumentException */
public class Refusal extends FriendlyException {

	public Refusal(String veryHelpfulMessage) {
		super(veryHelpfulMessage);
	}

	private static final long serialVersionUID = 1L;

}
