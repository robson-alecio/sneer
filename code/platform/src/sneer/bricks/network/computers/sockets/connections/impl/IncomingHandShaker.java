package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Seal;

class IncomingHandShaker {

	
	private static final Seals Seals = my(Seals.class);


	static Seal greet(ByteArraySocket socket) throws IOException {
		byte[] contactsSealBytes = identifyContact(socket);
		Seal contactsSeal = Seals.unmarshall(contactsSealBytes);

		rejectLoopback(contactsSeal);
		
		//Implement: Challenge pk.

		return contactsSeal;
	}


	static private void rejectLoopback(Seal peersSeal) throws IOException {
		if (peersSeal.equals(Seals.ownSeal()))
			throw new IOException("Socket identified as originating from yourself.");
	}


	static private byte[] identifyContact(ByteArraySocket socket) throws IOException {
		while (true) {
			byte[] header = socket.read();
			byte[] sealBytes = socket.read();
			
			if (Arrays.equals(header, ProtocolTokens.SNEER_WIRE_PROTOCOL_1))
				return sealBytes;
			
			socket.write(ProtocolTokens.FALLBACK);
		}
	}

}
