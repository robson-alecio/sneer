package sneer.pulp.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.own.name.OwnNameKeeper;

class ConnectionManagerImpl implements ConnectionManager {

	@Inject
	private static OwnNameKeeper _nameKeeper;

	private final Map<Contact, ByteConnection> _connectionsByContact = new HashMap<Contact, ByteConnection>();

	@Override
	public synchronized ByteConnectionImpl connectionFor(Contact contact) {
		ByteConnectionImpl result = (ByteConnectionImpl) _connectionsByContact.get(contact);
		if (result == null) {
			result = new ByteConnectionImpl("" + _nameKeeper.name(), contact);
			_connectionsByContact.put(contact, result);
		}
		return result;
	}

	@Override
	public void manageIncomingSocket(Contact contact, ByteArraySocket socket) {
		ByteConnectionImpl connection = connectionFor(contact);
		connection.manageIncomingSocket(socket);
	}

	@Override
	public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) {
		ByteConnectionImpl connection = connectionFor(contact);
		connection.manageOutgoingSocket(socket);
	}
}
