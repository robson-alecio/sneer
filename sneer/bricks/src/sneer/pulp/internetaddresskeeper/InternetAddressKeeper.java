package sneer.pulp.internetaddresskeeper;

import sneer.brickness.OldBrick;
import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListSignal;

public interface InternetAddressKeeper extends OldBrick {

	void add(Contact contact, String host, int port);
	
	ListSignal<InternetAddress> addresses();

}