package sneer.pulp.internetaddresskeeper;

import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListSignal;

public interface InternetAddressKeeper {

	void add(Contact contact, String host, int port);
	
	ListSignal<InternetAddress> addresses();

}
