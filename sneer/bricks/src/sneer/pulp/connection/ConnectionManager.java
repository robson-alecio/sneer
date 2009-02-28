package sneer.pulp.connection;

import sneer.brickness.Brick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.network.ByteArraySocket;

public interface ConnectionManager extends Brick {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);

	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	ByteConnection connectionFor(Contact contact);
}
