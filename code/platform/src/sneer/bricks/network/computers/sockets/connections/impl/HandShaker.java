package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;

class HandShaker {
	
	private final ByteArraySocket _socket;
	private Contact _contact;

	
	HandShaker(ByteArraySocket socket) {
		_socket = socket;

		try {
			_contact = tryToDetermineContact();
		} catch (IOException e) {
			my(Logger.class).log("Exception thrown by incoming socket: {}.", e.getMessage());
		}
		
		if (_contact == null) _socket.crash();
	}

	private Contact tryToDetermineContact() throws IOException {
		shakeHands();

		Seal peersPublicKey = peersPublicKey();	
		if (peersPublicKey.equals(my(Seals.class).ownSeal()))
			return null;
		//Implement: Challenge pk.

		return produceContact(peersPublicKey);
	}

	private Seal peersPublicKey() throws IOException {
		byte[] bytes = _socket.read();
		return my(Seals.class).unmarshall(bytes);
	}

	private Contact produceContact(Seal peersPublicKey) {
		return my(Seals.class).contactGiven(peersPublicKey);
	}

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

	Contact determineContact() {
		return _contact;
	}

	
}
