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

//package sneer.pulp.dyndns.ownaccount;
//
//public class Account {
//	
//	public Account(String pHost, String pUser, String pPassword) {
//		host = pHost;
//		user = pUser;
//		password = pPassword;
//	}
//	
//	/**
//	 * the full host name, for example, "test.dyndns.org"
//	 */
//	public final String host;
//	
//	/**
//	 * dyndns user name
//	 */
//	public final String user;
//	
//	/**
//	 * dyndns password
//	 */
//	public final String password;
//
//}