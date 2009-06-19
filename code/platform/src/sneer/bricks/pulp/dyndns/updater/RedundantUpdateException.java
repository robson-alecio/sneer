package sneer.bricks.pulp.dyndns.updater;

public class RedundantUpdateException extends UpdaterException {

	private static final String HELP = "The update changed no settings, and is considered abusive." +
			" This should not be happening and additional redundant updates might cause the hostname to become blocked.\n" +
			"Please notify your sneer expert friend.";

	public RedundantUpdateException() {
		super("Dyndns ip value redundantly updated", HELP);
	}
}
