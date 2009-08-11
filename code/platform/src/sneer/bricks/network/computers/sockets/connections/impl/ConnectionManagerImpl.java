package sneer.bricks.network.computers.sockets.connections.impl;

import java.io.IOException;

import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;
import sneer.foundation.lang.Closure;

class ConnectionManagerImpl implements ConnectionManager {

	@Override
	public ByteConnectionImpl connectionFor(final Contact contact) {
		return ConnectionsByContact.get(contact);
	}

	
	@Override
	public void manageIncomingSocket(final ByteArraySocket socket) {
		SocketCloser.closeIfUnsuccessful(socket, "Incoming socket closed.", new Closure<IOException>() { @Override public void run() throws IOException {
			Seal contactsSeal = IncomingHandShaker.greet(socket);
			TieBreaker.manageIncomingSocket(socket, contactsSeal);
		}});
	}


	@Override
	public void manageOutgoingSocket(final ByteArraySocket socket, final Contact contact) {
		SocketCloser.closeIfUnsuccessful(socket, "Outgoing socket closed.", new Closure<IOException>() { @Override public void run() throws IOException {
			OutgoingHandShaker.greet(socket);
			TieBreaker.manageOutgoingSocket(socket, contact);
		}});
	}

	
	@Override
	public void closeConnectionFor(Contact contact) {
		ByteConnectionImpl connection = ConnectionsByContact.remove(contact);
		if (connection != null) connection.close();
	}

}