package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.network.ByteArraySocket;
import sneer.contacts.Contact;

public class ConnectionManagerImpl implements ConnectionManager {

	private final Map<Contact, ConnectionImpl> _connectionsByContact = new HashMap<Contact, ConnectionImpl>();

	@Override
	public synchronized ConnectionImpl connectionFor(Contact contact) {
		ConnectionImpl result = _connectionsByContact.get(contact);
		if (result == null) {
			result = new ConnectionImpl();
			_connectionsByContact.put(contact, result);
		}
		return result;
	}

	@Override
	public void manageIncomingSocket(Contact contact, ByteArraySocket socket) {
		ConnectionImpl connection = connectionFor(contact);
		connection.manageIncomingSocket(socket);
		
	}

	@Override
	public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) {
		ConnectionImpl connection = connectionFor(contact);
		connection.manageOutgoingSocket(socket);
	}
}
