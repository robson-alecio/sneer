package sneer.pulp.dyndns.updater;

public class RedundantUpdateException extends UpdaterException {

	private static final String HELP = "nochg  	The update changed no settings, and is considered abusive." +
			" This should not be happening and additional nochg updates might cause the hostname to become blocked.\n" +
			"Please notify your sneer expert friend.";

	public RedundantUpdateException() {
		super("dyndns refused the update with 'nochg'", HELP);
	}
}
