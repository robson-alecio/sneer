package sneer.pulp.internetaddresskeeper;

import sneer.brickness.Brick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.collections.SetSignal;

@Brick
public interface InternetAddressKeeper {

	SetSignal<InternetAddress> addresses();

	void add(Contact contact, String host, int port);
	void remove(InternetAddress address);

}