package sneer.internetaddresskeeper;

import sneer.contacts.Contact;
import wheel.reactive.lists.ListSignal;

public interface InternetAddressKeeper {

	void add(Contact contact, String host, int port);
	
	ListSignal<InternetAddress> addresses();

}
