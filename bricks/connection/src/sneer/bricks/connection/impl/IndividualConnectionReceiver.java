package sneer.bricks.connection.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import sneer.bricks.connection.KeyManager;
import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.ContactId;
import sneer.lego.Brick;
import sneer.log.Logger;

class IndividualConnectionReceiver {

	private static final byte[] SNEER_WIRE_PROTOCOL_1 = toByteArray("Sneer Wire Protocol 1");
	private static final byte[] FALLBACK = toByteArray("Fallback");
	private static final byte[] OK = toByteArray("OK");
	
	
	@Brick
	private KeyManager _keyManager;
	
	@Brick
	private	Logger _logger;
	
	private final ByteArraySocket _socket;
	private byte[] _peersPublicKey;

	
	IndividualConnectionReceiver(ByteArraySocket socket) {
		_socket = socket;

		try {
			tryToServe();
		} catch (Exception e) {
			_logger.info("Exception thrown by incoming socket.", e);
			_socket.crash();
		}
	}

	private void tryToServe() throws IOException {
		shakeHands();

		_peersPublicKey = _socket.read();
		if (!tryToAuthenticate()) return;
		
		ContactId contactId = _keyManager.contactIdGiven(_peersPublicKey);
//		if (contactId == null)
//			contactId = createUnconfirmedContact();
		
//		_connectionManager.manage(_socket, contactId);
	}


	private static byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}


	private boolean tryToAuthenticate() {
		byte[] ownPublicKey = _keyManager.ownPublicKey();
		if (Arrays.equals(_peersPublicKey, ownPublicKey))
			return false;
		
		//Implement: Challenge! :)
		return true;
	}


	private void shakeHands() throws IOException {
		while (true) {
			byte[] header = _socket.read();
			if (Arrays.equals(header, SNEER_WIRE_PROTOCOL_1)) {
				_socket.write(OK);
				return;
			}
			discardFirstDataPacket();
			_socket.write(FALLBACK);
		}
	}
		
	private void discardFirstDataPacket() throws IOException {
		_socket.read();
	}

	
}
