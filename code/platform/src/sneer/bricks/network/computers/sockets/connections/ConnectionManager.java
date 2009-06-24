package sneer.bricks.network.computers.sockets.connections;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Brick;

@Brick
public interface ConnectionManager {

	void manageIncomingSocket(Contact contact, ByteArraySocket socket);
	void manageOutgoingSocket(Contact contact, ByteArraySocket socket);

	ByteConnection connectionFor(Contact contact);
	void closeConnectionFor(Contact contact);
}
