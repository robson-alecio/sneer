package sneer.bricks.network.computers.sockets.connections;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Brick;

@Brick
public interface ConnectionManager {

	void manageIncomingSocket(ByteArraySocket socket);
	void manageOutgoingSocket(ByteArraySocket socket, Contact contact);

	ByteConnection connectionFor(Contact contact);

	void closeConnectionFor(Contact contact);

}
