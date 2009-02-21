package sneer.pulp.internetaddresskeeper;

import sneer.kernel.container.Brick;
import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListSignal;

public interface InternetAddressKeeper extends Brick {

	void add(Contact contact, String host, int port);
	
	ListSignal<InternetAddress> addresses();

}