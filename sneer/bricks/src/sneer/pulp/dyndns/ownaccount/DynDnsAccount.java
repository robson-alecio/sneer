package sneer.pulp.dyndns.ownaccount;

public class DynDnsAccount {
	
	public DynDnsAccount(String pHost, String pDynDnsUser, String pPassword) {
		host = pHost;
		dynDnsUser = pDynDnsUser;
		password = pPassword;
	}
	
	/** Example: "test.dyndns.org"	 */
	public final String host;
	
	public final String dynDnsUser;
	public final String password;

}