package sneer.bricks.pulp.internetaddresskeeper;

import sneer.bricks.network.social.Contact;

public interface InternetAddress {

	Contact contact();
	
	String host();
	
	int port();
}
