package sneer.pulp.internetaddresskeeper;

import sneer.brickness.Brick;
import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListSignal;

@Brick
public interface InternetAddressKeeper {

	ListSignal<InternetAddress> addresses();

	void add(Contact contact, String host, int port);
	void remove(InternetAddress address);

}