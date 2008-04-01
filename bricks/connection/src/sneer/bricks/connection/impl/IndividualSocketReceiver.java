package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.keymanager.ContactAlreadyHadAKey;
import sneer.bricks.keymanager.KeyBelongsToOtherContact;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.ContainerUtils;
import sneer.log.Logger;
import wheel.lang.exceptions.IllegalParameter;

class IndividualSocketReceiver {

	@Brick
	private KeyManager _keyManager;
	
	@Brick
	private ContactManager _contactManager;
	
	@Brick
	private ConnectionManager _connectionManager;
	
	@Brick
	private	Logger _logger;
	
	private final ByteArraySocket _socket;

	
	IndividualSocketReceiver(ByteArraySocket socket) {
		ContainerUtils.inject(this);
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

		byte[] peersPublicKey = _socket.read();	

		Contact contact = produceContact(peersPublicKey);
		if(contact != null)
			_connectionManager.manageIncomingSocket(contact, _socket);
		else
			_socket.crash();
	}

	private Contact produceContact(byte[] peersPublicKey) {
		while(true) {
			Contact contact = _keyManager.contactGiven(peersPublicKey);
			if (contact == null) {
				contact = createUnconfirmedContact();
				try {
					_keyManager.addKey(contact, peersPublicKey);
					return contact;
				} catch (ContactAlreadyHadAKey e) {
					//how did this happen?
					_logger.error("Error producing contact",e);
					return null;
				} catch (KeyBelongsToOtherContact e) {
					//Other thread assigned this pk to other contact. Try again
					_logger.info("Key belongs to other contact. Trying again...",e);
				}
			}
		}
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
