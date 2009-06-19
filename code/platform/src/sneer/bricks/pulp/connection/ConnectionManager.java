package sneer.bricks.pulp.connection;

import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Brick;

@Brick
public interface ConnectionManager {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);

	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	ByteConnection connectionFor(Contact contact);

	void closeConnectionFor(Contact contact);
}
