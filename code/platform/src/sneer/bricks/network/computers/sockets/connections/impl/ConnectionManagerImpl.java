package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.foundation.lang.Producer;

class ConnectionManagerImpl implements ConnectionManager {

	private final OwnNameKeeper _nameKeeper = my(OwnNameKeeper.class);

	private final CacheMap<Contact, ByteConnectionImpl> _connectionsByContact = my(CacheMaps.class).newInstance();

	@Override
	public synchronized ByteConnectionImpl connectionFor(final Contact contact) {
		return _connectionsByContact.get(contact, new Producer<ByteConnectionImpl>() { @Override public ByteConnectionImpl produce() {
			return new ByteConnectionImpl("" + _nameKeeper.name(), contact);
		}});
	}

	@Override
	public void manageIncomingSocket(ByteArraySocket socket) {
		Contact contact = new HandShaker(socket).determineContact();
		if (contact == null) return;
		
		connectionFor(contact).manageIncomingSocket(socket);

	}

	@Override
	public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) {
		connectionFor(contact).manageOutgoingSocket(socket);
	}

	@Override
	synchronized public void closeConnectionFor(Contact contact) {
		ByteConnectionImpl connection = _connectionsByContact.remove(contact);
		if (connection == null) return;
		connection.close();
	}	
}