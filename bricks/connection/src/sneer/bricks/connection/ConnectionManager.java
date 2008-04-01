package sneer.bricks.connection;

import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.Contact;

public interface ConnectionManager {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);

	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	Connection connectionFor(Contact contact);
}
