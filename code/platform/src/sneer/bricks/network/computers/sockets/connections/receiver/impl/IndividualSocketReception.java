package sneer.bricks.network.computers.sockets.connections.receiver.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.PublicKey;

class IndividualSocketReception {
	
	private final ByteArraySocket _socket;

	IndividualSocketReception(ByteArraySocket socket) {
		_socket = socket;

		try {
			if (!tryToServe()) _socket.crash();
		} catch (Exception e) {
			my(Logger.class).logShort(e, "Exception thrown by incoming socket.");
			_socket.crash();
		}
	}

	private boolean tryToServe() throws IOException {
		shakeHands();

		PublicKey peersPublicKey = peersPublicKey();	

		if (peersPublicKey.equals(my(KeyManager.class).ownPublicKey()))
			return false;
			
		//Implement: Challenge pk.

		_socket.write(ProtocolTokens.OK);

		Contact contact = produceContact(peersPublicKey);
		my(ConnectionManager.class).manageIncomingSocket(contact, _socket);
		return true;
	}

	private PublicKey peersPublicKey() throws IOException {
		byte[] bytes = _socket.read();
		return my(KeyManager.class).unmarshall(bytes);
	}

	private Contact produceContact(PublicKey peersPublicKey) {
//		return my(KeyManager.class).contactGiven(peersPublicKey, new Producer<Contact>(){@Override public Contact produce() {
//			return createUnconfirmedContact();
//		}});
		
		return my(KeyManager.class).contactGiven(peersPublicKey);
	}

//	private Contact createUnconfirmedContact() {
//		String baseNick = "Unconfirmed";   //"Unconfirmed (2)", "Unconfirmed (3)", etc.
//		
//		Contact result = tryToCreate(baseNick);
//		if (result != null) return result;
//		
//		int i = 2;
//		while (tryToCreate(baseNick + " (" + i + ")") == null)
//			i++;
//		
//		return result;
//	}
//
//	private Contact tryToCreate(String nickname) {
//		try {
//			return my(ContactManager.class).addContact(nickname);
//		} catch (Refusal e) {
//			return null;
//		}
//	}


	private void shakeHands() throws IOException {
		while (true) {
			byte[] header = _socket.read();
			if (Arrays.equals(header, ProtocolTokens.SNEER_WIRE_PROTOCOL_1))
				return;

			discardFirstDataPacket();
			_socket.write(ProtocolTokens.FALLBACK);
		}
	}
		
	private void discardFirstDataPacket() throws IOException {
		_socket.read();
	}

	
}
