package sneer.bricks.internetaddresskeeper;

import sneer.bricks.contacts.Contact;

public interface InternetAddress {

	Contact contact();
	
	String host();
	
	int port();
}
