package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;
import sneer.foundation.lang.Closure;

class ConnectionManagerImpl implements ConnectionManager {

	static private final Seals Seals = my(Seals.class);


	@Override
	public ByteConnectionImpl connectionFor(final Contact contact) {
		return ConnectionsByContact.get(contact);
	}

	
	@Override
	public void manageIncomingSocket(final ByteArraySocket socket) {
		SocketCloser.closeIfUnsuccessful(socket, "Incoming socket closed.", new Closure<IOException>() { @Override public void run() throws IOException {
			Seal contactsSeal = IncomingHandShaker.greet(socket);
			breakTieIfNecessary(contactsSeal, socket, true);
		}});
	}


	@Override
	public void manageOutgoingSocket(final ByteArraySocket socket, final Contact contact) {
		SocketCloser.closeIfUnsuccessful(socket, "Outgoing socket closed.", new Closure<IOException>() { @Override public void run() throws IOException {
			OutgoingHandShaker.greet(socket);
			breakTieIfNecessary(Seals.sealGiven(contact), socket, false);
		}});
	}

	
	@Override
	public void closeConnectionFor(Contact contact) {
		ByteConnectionImpl connection = ConnectionsByContact.remove(contact);
		if (connection != null) connection.close();
	}
	
	
	private void breakTieIfNecessary(Seal contactsSeal, ByteArraySocket socket, boolean isIncoming) {
		Contact contact = Seals.contactGiven(contactsSeal);
		ByteConnectionImpl connection = ConnectionsByContact.get(contact);
		SocketHolder socketHolder = connection.socketHolder();
		
		synchronized (socketHolder) {
			if (socketHolder.socket() == null) {
				socketHolder.setSocket(socket);
				return;
			}
			
			boolean mustOverride = isIncoming
				? compare(Seals.ownSeal(), contactsSeal)
				: compare(contactsSeal, Seals.ownSeal());
				
			if (!mustOverride) return;
			socketHolder.close("Existing socket overriden by new one.");
			socketHolder.setSocket(socket);
		}

	}


	private boolean compare(Seal seal1, Seal seal2) {
		byte[] bytes1 = seal1.bytes();
		byte[] bytes2 = seal2.bytes();
		
		for (int i = 0; i < bytes1.length; i++) {
			if (bytes1[i] == bytes2[i]) continue;
			return bytes1[i] > bytes2[i];
		}
		
		throw new IllegalStateException("Two Seal are the same.");
	}


}