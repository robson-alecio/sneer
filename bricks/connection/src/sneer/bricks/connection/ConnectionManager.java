package sneer.bricks.connection;

import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.Contact;

public interface ConnectionManager {

	void manageIncomingConnection(Contact contact, ByteArraySocket socket);

}
