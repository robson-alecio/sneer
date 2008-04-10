package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.contacts.ContactManager;
import sneer.bricks.mesh.Mesh;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.Startable;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.maps.impl.SimpleMapReceiver;

public class MeshImpl implements Mesh, Startable {

	private final Map<Contact, ContactProxy> _proxiesByContact = new HashMap<Contact, ContactProxy>();
	
	@SuppressWarnings("unused")
	private SimpleMapReceiver<Contact, Connection> _connectionReceiverToAvoidGC;

	@Inject
	private ConnectionManager _connectionManager;

	@Inject
	private ContactManager _contactManager;

	@Inject
	private Injector _injector;

	@Override
	public void start() throws Exception {
		_connectionReceiverToAvoidGC = new SimpleMapReceiver<Contact, Connection>(_connectionManager.connections()){

			@Override
			protected void entryPresent(Contact contact, Connection connection) {
				startServing(connection);
			}

			@Override
			public void entryAdded(Contact contact, Connection connection) {
				startServing(connection);
			}

			@Override
			public void entryToBeRemoved(Contact contact, Connection connection) {
				stopServing(connection);
			}

		};
	}

	private void startServing(Connection connection) {
		Threads.startDaemon(new IndividualConnectionHandler(connection));
	}

	private void stopServing(Connection connection) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
	
	@Override
	public <T> Signal<T> findSignal(String nicknamePath, String signalPath) {
		String[] path = nicknamePath.split("/", 1);
		String head = path[0];
		String tail = path.length > 1
			? path[1]
			: "";
			
		Contact immediateContact = _contactManager.contactGiven(head);
		ContactProxy proxy = produceProxyFor(immediateContact);
		
		return proxy.findSignal(tail, signalPath);
	}

	private ContactProxy produceProxyFor(Contact contact) {
		synchronized (_proxiesByContact) {
			ContactProxy proxy = _proxiesByContact.get(contact);
			if (proxy == null) {
				proxy = new ContactProxy(_injector, contact);
				_proxiesByContact.put(contact, proxy);
			}
			return proxy;
		}
	}
}
