package sneer.internetaddresskeeper;

import sneer.contacts.Contact;

public interface InternetAddress {

	Contact contact();
	
	String host();
	
	int port();
}
