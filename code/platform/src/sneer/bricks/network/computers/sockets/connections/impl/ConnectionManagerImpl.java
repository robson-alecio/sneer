package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;
import sneer.foundation.lang.Closure;

class ConnectionManagerImpl implements ConnectionManager {

	static final WeakContract crashingContract = my(Threads.class).crashing().addPulseReceiver(new Runnable() { @Override public void run() {
		for (ByteConnectionImpl victim : ConnectionsByContact.all())
			victim.close();
	}});
	
	
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