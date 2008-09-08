package sneer.pulp.dyndns.ownaccount;

public interface Account {
	
	/**
	 * the full host name, for example, "test.dyndns.org"
	 */
	String host();
	
	/**
	 * dyndns user name
	 */
	String user();
	
	/**
	 * dyndns password
	 */
	String password();

}
