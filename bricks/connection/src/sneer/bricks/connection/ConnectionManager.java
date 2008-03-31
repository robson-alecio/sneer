package sneer.bricks.connection;

import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.Contact;
import wheel.reactive.Signal;

public interface ConnectionManager {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);

	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	Signal<Boolean> isSocketNeededFor(Contact contact);

}
