package sneer.bricks.pulp.dyndns.updater;

public class ServerErrorException extends UpdaterException {
	
	private static final String HELP = "The dyndns server returned a code that indicates server errors that will have to be investigated.\n" +
			" Please contact DynDNS support.\n" +
			" Sneer will retry every 30 minutes.\n";

	public ServerErrorException(String code) {
		super("dyndns error: " + code, HELP);
	}

}
