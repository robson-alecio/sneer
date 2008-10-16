package sneer.pulp.dyndns.updater;

public class BadAuthException extends UpdaterException {
	
	private static final String HELP = "Please check your dyndns account information.";

	public BadAuthException() {
		super("The username and password pair do not match a real user", HELP);
	}

}
