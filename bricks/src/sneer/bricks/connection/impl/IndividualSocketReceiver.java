package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.log.Logger;
import sneer.bricks.network.ByteArraySocket;
import sneer.lego.Inject;
import wheel.lang.Functor;
import wheel.lang.exceptions.IllegalParameter;

class IndividualSocketReceiver {

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private ContactManager _contactManager;
	
	@Inject
	static private ConnectionManager _connectionManager;
	
	@Inject
	static private Crypto _crypto;

	@Inject
	static private Logger _logger;
	
	
	private final ByteArraySocket _socket;
	
	IndividualSocketReceiver(ByteArraySocket socket) {
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

		byte[] publicKeyBytes = _socket.read();
		Sneer1024 peersPublicKey = _crypto.wrap(publicKeyBytes);	

		Contact contact = produceContact(peersPublicKey);
		_connectionManager.manageIncomingSocket(contact, _socket);
	}

	private Contact produceContact(Sneer1024 peersPublicKey) {
		return _keyManager.contactGiven(peersPublicKey, new Functor<Sneer1024, Contact>(){@Override public Contact evaluate(Sneer1024 value) {
			return createUnconfirmedContact();
		}});
	}

	private Contact createUnconfirmedContact() {
		String baseNick = "Unconfirmed";   //"Unconfirmed (2)", "Unconfirmed (3)", etc.
		
		Contact result = tryToCreate(baseNick);
		if (result != null) return result;
		
		int i = 2;
		while (tryToCreate(baseNick + " (" + i + ")") == null)
			i++;
		
		return result;
	}

	private Contact tryToCreate(String nickname) {
		if (_contactManager.isNicknameAlreadyUsed(nickname))
			return null;
		
		try {
			return _contactManager.addContact(nickname);
		} catch (IllegalParameter e) {
			return null;
		}
	}


	private void shakeHands() throws IOException {
		while (true) {
			byte[] header = _socket.read();
			if (Arrays.equals(header, ProtocolTokens.SNEER_WIRE_PROTOCOL_1)) {
				_socket.write(ProtocolTokens.OK);
				return;
			}
			discardFirstDataPacket();
			_socket.write(ProtocolTokens.FALLBACK);
		}
	}
		
	private void discardFirstDataPacket() throws IOException {
		_socket.read();
	}

	
}
