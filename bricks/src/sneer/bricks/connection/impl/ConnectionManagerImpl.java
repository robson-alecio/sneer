package sneer.bricks.connection.impl;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.network.ByteArraySocket;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.maps.MapSource;
import wheel.reactive.maps.impl.MapSourceImpl;

public class ConnectionManagerImpl implements ConnectionManager {

	private final MapSource<Contact, Connection> _connectionsByContact = new MapSourceImpl<Contact, Connection>();

	@Override
	public MapSignal<Contact, Connection> connections() {
		return _connectionsByContact.output();
	}

	@Override
	public synchronized ConnectionImpl connectionFor(Contact contact) {
		ConnectionImpl result = (ConnectionImpl) _connectionsByContact.get(contact);
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
