package sneer.pulp.internetaddresskeeper;

import sneer.pulp.contacts.Contact;

public interface InternetAddress {

	Contact contact();
	
	String host();
	
	int port();
}
