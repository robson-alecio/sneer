package sneer.bricks.connection;

import sneer.bricks.contacts.Contact;
import sneer.bricks.network.ByteArraySocket;
import wheel.reactive.maps.MapSignal;

public interface ConnectionManager {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);

	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	Connection connectionFor(Contact contact);

	MapSignal<Contact, Connection> connections();
}
