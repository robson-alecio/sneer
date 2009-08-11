package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;

/** When two parties simultaneously open sockets to each other, this guy decides which socket to use by comparing an arbitrary symmetry breaker: each party's seal.*/
class TieBreaker {

	static private final Seals Seals = my(Seals.class);

	
	static void manageIncomingSocket(ByteArraySocket newSocket, Seal contactsSeal) throws IOException {
		breakTie(newSocket, Seals.contactGiven(contactsSeal), true);
	}

	
	static void manageOutgoingSocket(ByteArraySocket newSocket, Contact contact) throws IOException {
		breakTie(newSocket, contact, false);
	}

	
	static private void breakTie(ByteArraySocket newSocket, Contact contact, boolean isIncoming) throws IOException {
		if (mySealIsGreaterThanHis(contact))
			confirm(newSocket, contact, isIncoming);
		else
			waitForConfirmation(newSocket, contact, isIncoming);
	}


	private static boolean mySealIsGreaterThanHis(Contact contact) {
		byte[] bytes1 = Seals.ownSeal().bytes();
		byte[] bytes2 = Seals.sealGiven(contact).bytes();
		
		if (bytes1.length != bytes2.length)
			return bytes1.length > bytes2.length;
		
		for (int i = 0; i < bytes1.length; i++) {
			if (bytes1[i] == bytes2[i]) continue;
			return bytes1[i] > bytes2[i];
		}
		
		throw new IllegalStateException("Both seals are the same.");
	}

	
	private static void confirm(ByteArraySocket newSocket, Contact contact, boolean isIncoming) throws IOException {
		SocketHolder socketHolder = socketHolderFor(contact);
		
		synchronized (socketHolder) {
			if (socketHolder.socket() != null)
				throw new IOException("New " + toString(isIncoming) + " socket closed because there already was an active socket for the same contact.");
			
			newSocket.write(ProtocolTokens.CONFIRMED);
			socketHolder.setSocket(newSocket);
		}
	}

	
	private static void waitForConfirmation(ByteArraySocket newSocket, Contact contact, boolean isIncoming) throws IOException {
		if (!Arrays.equals(newSocket.read(), ProtocolTokens.CONFIRMED))
			throw new IOException("New " + toString(isIncoming) + " socket closed because instead of receiving a confirmation packet it received a different packet.");
		
		socketHolderFor(contact).overrideSocket(newSocket);
	}


	static private SocketHolder socketHolderFor(Contact contact) {
		return ConnectionsByContact.get(contact).socketHolder();
	}

	
	static private String toString(boolean isIncoming) {
		return (isIncoming ? "incoming" : "outgoing");
	}
	
}
