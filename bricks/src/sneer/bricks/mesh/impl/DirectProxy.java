package sneer.bricks.mesh.impl;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.bricks.serialization.Serializer;
import sneer.lego.Inject;
import sneer.lego.Injector;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

class DirectProxy extends Proxy {

	@Inject
	private ConnectionManager _connectionManager;
	
	@Inject
	private Serializer _serializer;

	@Inject
	private Me _me;
	

	private final Connection _connection;

	private final PriorityQueue<byte[]> _priorityQueue = new PriorityQueue<byte[]>(10);

	private volatile boolean _isCrashed = false;

	private List<Object> _scoutsToAvoidGC = new ArrayList<Object>();



	DirectProxy(Injector injector, Contact contact) {
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
			ambassador = _serializer.deserialize(packetReceived, DirectProxy.class.getClassLoader());
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

	void serveSubscriptionTo(ArrayList<String> nicknamePath, String signalPath) {
		Signal<Object> signal = findParty(nicknamePath).signal(signalPath);
		signal.addReceiver(createScoutFor(nicknamePath, signalPath));
	}

	private Omnivore<Object> createScoutFor(final ArrayList<String> nicknamePath, final String signalPath) {
		Omnivore<Object> result = new Omnivore<Object>() {@Override public void consume(Object newValue) {
			send(new Notification(nicknamePath, signalPath, newValue));
		}};
		_scoutsToAvoidGC.add(result); //Fix: Avoid leak.
		return result;
	}

	private Party findParty(ArrayList<String> nicknamePath) {
		Party result = _me;
		for (String nickname : nicknamePath)
			result = result.navigateTo(nickname);
		return result;
	}

	void handleNotification(ArrayList<String> nicknamePath, String signalPath, Object newValue) {
		Party candidate = this;
		for (String nickname : nicknamePath)
			candidate = candidate.navigateTo(nickname);
		((Proxy)candidate).handleNotification(signalPath, newValue);
	}

	@Override
	AbstractParty produceProxyFor(String nickname) {
		return new RemoteProxy(this, nickname);
	}

	@Override
	void subscribeTo(ArrayList<String> nicknamePath, String remoteSignalPath) {
		send(new Subscription(nicknamePath, remoteSignalPath));
	}

}