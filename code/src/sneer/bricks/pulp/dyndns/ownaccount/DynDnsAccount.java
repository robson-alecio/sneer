package sneer.bricks.pulp.dyndns.ownaccount;

import sneer.foundation.brickness.Tuple;

public class DynDnsAccount extends Tuple {
	
	public DynDnsAccount(String host_, String dynDnsUser_, String password_) {
		host = host_;
		user = dynDnsUser_;
		password = password_;
	}
	
	/** Example: "test.dyndns.org"	 */
	public final String host;
	
	public final String user;
	public final String password;

}