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

	void serveSubscriptionTo(Sneer1024 publicKey, String signalPath) {
		Party target = produceParty(publicKey);
		Signal<Object> signal = target.signal(signalPath);

		signal.addReceiver(createScoutFor(publicKey, signalPath));
	}


	private AbstractParty produceParty(Sneer1024 pk) {
		return (AbstractParty)_keyManager.partyGiven(pk, ProxyFactory.INSTANCE);
	}


	void handleNotification(Sneer1024 publicKey, String signalPath, Object newValue) {
		AbstractParty target = produceParty(publicKey);
		if (target instanceof Me) {
			_logger.info("Illegal notification.");
			return;
		}
		((Proxy)target).handleNotification(signalPath, newValue);
	}


	void subscribeTo(Sneer1024 publicKey, String remoteSignalPath) {
		send(new Subscription(publicKey, remoteSignalPath));
	}

	
}
