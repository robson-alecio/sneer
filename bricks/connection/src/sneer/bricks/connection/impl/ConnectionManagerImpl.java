package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.Contact;
import wheel.reactive.Signal;

public class ConnectionManagerImpl implements ConnectionManager {

	private final Map<Contact, Connection> _connectionsByContact = new HashMap<Contact, Connection>();

	@Override
	public Signal<Boolean> isOnline(Contact contact) {
		return connectionFor(contact).isOnline();
	}

	private synchronized Connection connectionFor(Contact contact) {
		Connection result = _connectionsByContact.get(contact);
		if (result == null) {
			result = new Connection();
			_connectionsByContact.put(contact, result);
		}
		return result;
	}

	@Override
	public void manageIncomingSocket(Contact contact, ByteArraySocket socket) {
		Connection connection = connectionFor(contact);
		connection.manageIncomingSocket(socket);
		
	}

	@Override
	public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) {
		Connection connection = connectionFor(contact);
		connection.manageOutgoingSocket(socket);
	}

	


}
