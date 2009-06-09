package sneer.bricks.pulp.internetaddresskeeper;

import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface InternetAddressKeeper {

	SetSignal<InternetAddress> addresses();

	void add(Contact contact, String host, int port);
	void remove(InternetAddress address);

}