package sneer.bricks.pulp.dyndns.updater;

public class UnexpectedResponseException extends UpdaterException {

	private static final String HELP = "An unexpected response was received from dyndns.\n" +
			"Sneer will stop updating your dyndns host.\n" +
			"Please beg your sneer expert friend for help.";

	public UnexpectedResponseException(String message) {
		super(message, HELP);
	}

}
