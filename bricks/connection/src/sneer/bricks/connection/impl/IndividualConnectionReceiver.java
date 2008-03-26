package sneer.bricks.connection.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import sneer.bricks.connection.KeyManager;
import sneer.bricks.network.ByteArraySocket;
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

	
	IndividualConnectionReceiver(ByteArraySocket socket) {
		_socket = socket;

		try {
			tryToServe();
		} catch (IOException e) {
			_logger.info("IOException thrown by incoming socket.", e);
		} finally {
			_socket.crash();
		}
	}

	private void tryToServe() throws IOException {
		shakeHands();

		byte[] peersPublicKey = _socket.read();
		if (!tryToAuthenticate(peersPublicKey)) return;
		
		//ContactId id = _keyManager.getContact(peersPublicKey);
	}

	private static byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}


	private boolean tryToAuthenticate(byte[] peersPublicKey) {
		byte[] ownPublicKey = _keyManager.ownPublicKey();
		if (Arrays.equals(peersPublicKey, ownPublicKey))
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
