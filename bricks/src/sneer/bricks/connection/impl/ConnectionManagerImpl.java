package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Me;
import sneer.bricks.network.ByteArraySocket;
import sneer.lego.Inject;

public class ConnectionManagerImpl implements ConnectionManager {

	@Inject
	private Me _me;

	private final Map<Contact, Connection> _connectionsByContact = new HashMap<Contact, Connection>();


	@Override
	public synchronized ConnectionImpl connectionFor(Contact contact) {
		ConnectionImpl result = (ConnectionImpl) _connectionsByContact.get(contact);
		if (result == null) {
			result = new ConnectionImpl("" + _me.signal("Name"), contact);
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
