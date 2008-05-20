package sneer.bricks.mesh.impl;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.log.Logger;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.bricks.serialization.Serializer;
import sneer.lego.Inject;
import sneer.lego.Injector;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class SignalConnection {

	@Inject
	private ConnectionManager _connectionManager;
	
	@Inject
	private Serializer _serializer;


	private final Connection _connection;

	private final PriorityQueue<byte[]> _priorityQueue = new PriorityQueue<byte[]>(10);

	private volatile boolean _isCrashed = false;

	private List<Object> _scoutsToAvoidGC = new ArrayList<Object>();

	@Inject
	private KeyManager _keyManager;

	@Inject
	private Logger _logger;


	SignalConnection(Injector injector, Contact contact) {
		injector.inject(this);
		_connection = _connectionManager.connectionFor(contact);
		_connection.setReceiver(new Omnivore<byte[]>(){public void consume(byte[] packetReceived) {
			receive(packetReceived);
		}});
		startSender();
	}
	
	private void receive(byte[] packetReceived) {
		Object ambassador;
		try {
			ambassador = _serializer.deserialize(packetReceived, SignalConnection.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}

		try {
			((Ambassador)ambassador).visit(this);
		} catch (ClassCastException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private void startSender() {
		Threads.startDaemon(new Runnable() { public void run() {
			while (!_isCrashed) {
				byte[] toSend = _priorityQueue.waitForNext();
				_connection.send(toSend);
			}
		}});
	}

	private void send(Object object) {
		byte[] packet = serialize(object);

		_priorityQueue.add(packet, 2);
	}


	private byte[] serialize(Object object) {
		try {
			return _serializer.serialize(object);
		} catch (NotSerializableException e) {
			throw new RuntimeException(e);
		}
	}

	void crash() {
		_isCrashed = true;
	}


	private Omnivore<Object> createScoutFor(final Sneer1024 publicKey, final String signalPath) {
		Omnivore<Object> result = new Omnivore<Object>() {@Override public void consume(Object newValue) {
			send(new Notification(publicKey, signalPath, newValue));
		}};
		_scoutsToAvoidGC.add(result); //Fix: This is a Leak.
		return result;
	}

	private void createScoutForContacts(final Sneer1024 publicKey, ListSignal<Contact> contacts) {
		SimpleListReceiver<Contact> scout = new SimpleListReceiver<Contact>(contacts) {

			@Override
			protected void elementPresent(Contact contact) {
				elementAdded(contact);
			}
			
			@Override
			protected void elementAdded(Contact newContact) {
				maintainChainOfIntermediaries(publicKey, newContact);
				
				send(new NotificationOfContactAdded(publicKey, toRemoteContact(newContact)));
			}

			@Override
			protected void elementToBeRemoved(Contact contact) {
				send(new NotificationOfContactRemoved(publicKey, toRemoteContact(contact)));
			}
		};

		_scoutsToAvoidGC.add(scout); //Fix: This is a Leak.
	}

	private void maintainChainOfIntermediaries(final Sneer1024 publicKey, Contact contact) {
		AbstractParty intermediary = produceParty(publicKey);
		Sneer1024 contactPK = intermediary.producePublicKeyFor(contact);
		AbstractParty contactProxy = (AbstractParty) _keyManager.partyGiven(contactPK, ProxyFactory.INSTANCE);
		contactProxy.addIntermediaryIfNecessary(intermediary);
	}

	private RemoteContact toRemoteContact(Contact contact) {
		Sneer1024 pk = _keyManager.keyGiven(contact);
		return new RemoteContact(pk, contact.nickname().currentValue());
	}

	private AbstractParty produceParty(Sneer1024 pk) {
		return (AbstractParty)_keyManager.partyGiven(pk, ProxyFactory.INSTANCE);
	}


	void handleNotification(Sneer1024 publicKey, String signalPath, Object newValue) {
		Proxy target = produceProxy(publicKey);
		if (target == null) return;
		
		target.handleNotification(signalPath, newValue);
	}

	void handleNotificationOfContactAdded(Sneer1024 publicKey, RemoteContact newContact) {
		Proxy target = produceProxy(publicKey);
		if (target == null) return;
		
		target.handleNotificationOfContactAdded(newContact);
	}
	
	void handleNotificationOfContactRemoved(Sneer1024 publicKey,	RemoteContact contact) {
		Proxy target = produceProxy(publicKey);
		if (target == null) return;
		
		target.handleNotificationOfContactRemoved(contact);
	}
	

	private Proxy produceProxy(Sneer1024 publicKey) {
		AbstractParty target = produceParty(publicKey);
		if (target instanceof Me) {
			_logger.info("Illegal notification.");
			return null;
		}
		return (Proxy)target;
	}
	

	void subscribeTo(Sneer1024 publicKey, String remoteSignalPath) {
		send(new Subscription(publicKey, remoteSignalPath));
	}

	void subscribeToContacts(Sneer1024 targetPK) {
		send(new SubscriptionToContacts(targetPK));
	}

	void serveSubscriptionTo(Sneer1024 publicKey, String signalPath) {
		Party target = produceParty(publicKey);
		Signal<Object> signal = target.signal(signalPath);
		
		signal.addReceiver(createScoutFor(publicKey, signalPath));
	}
	
	public void serveSubscriptionToContacts(Sneer1024 publicKey) {
		Party target = produceParty(publicKey);
		ListSignal<Contact> contacts = target.contacts();

		createScoutForContacts(publicKey, contacts);
	}

	
}