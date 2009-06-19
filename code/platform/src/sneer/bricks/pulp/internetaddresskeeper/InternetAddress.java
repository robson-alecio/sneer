package sneer.bricks.pulp.internetaddresskeeper;

import sneer.bricks.pulp.contacts.Contact;

public interface InternetAddress {

	Contact contact();
	
	String host();
	
	int port();
}
