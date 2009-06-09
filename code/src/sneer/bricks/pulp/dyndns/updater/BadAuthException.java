package sneer.bricks.pulp.dyndns.updater;

public class BadAuthException extends UpdaterException {
	
	private static final String HELP = "The username and password pair do not match a real user. Please check your dyndns account information.";

	public BadAuthException() {
		super("Bad Dyndns Authorization", HELP);
	}

}
