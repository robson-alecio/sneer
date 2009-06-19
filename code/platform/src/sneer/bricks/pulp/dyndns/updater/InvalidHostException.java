package sneer.bricks.pulp.dyndns.updater;

public class InvalidHostException extends UpdaterException {
	
	private static final String HELP = "notfqdn  	The hostname specified is not" +
				" a fully-qualified domain name (not in the form hostname.dyndns.org or domain.com).\n" +
			"nohost 	The hostname specified does not exist in this" +
				" user account (or is not in the service specified in the system parameter)\n" +
			"numhost 	Too many hosts (more than 20) specified in an" +
				" update. Also returned if trying to update a round robin (which is not allowed)\n" +
			"abuse 	The hostname specified is blocked for update abuse.";

	public InvalidHostException(String code) {
		super("dyndns error: " + code, HELP);
	}
}
