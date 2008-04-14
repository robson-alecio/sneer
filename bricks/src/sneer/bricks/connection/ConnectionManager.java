package sneer.bricks.connection;

import sneer.bricks.contacts.Contact;
import sneer.bricks.network.ByteArraySocket;

public interface ConnectionManager {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);

	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	Connection connectionFor(Contact contact);
}
