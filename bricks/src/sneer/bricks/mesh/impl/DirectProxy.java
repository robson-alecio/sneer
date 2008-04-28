package sneer.bricks.mesh.impl;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.serialization.Serializer;
import sneer.lego.Inject;
import sneer.lego.Injector;
import spikes.legobricks.name.OwnNameKeeper;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class DirectProxy extends AbstractParty {

	@Inject
	private ConnectionManager _connectionManager;
	
	@Inject
	private Serializer _serializer;

	@Inject
	private OwnNameKeeper _ownNameKeeper;

	private final Connection _connection;

	private final Map<String, Register<Object>> _registersByRemotePath = new HashMap<String, Register<Object>>();

	private final PriorityQueue<byte[]> _priorityQueue = new PriorityQueue<byte[]>(10);

	private volatile boolean _isCrashed = false;

	private final Omnivore<String> _nameReceiverToAvoidGC = new Omnivore<String>(){public void consume(String newName) {
		send(new Notification("Name", newName));
	}};



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

	private <T> Register<T> produceRegisterFor(String remoteSignalPath) {
		Register<Object> register = _registersByRemotePath.get(remoteSignalPath);
		if (register == null) {
			register = new RegisterImpl<Object>(null);
			_registersByRemotePath.put(remoteSignalPath, register);
			subscribeTo(remoteSignalPath); 
		}
		return Casts.uncheckedGenericCast(register);
	}

	private void subscribeTo(String signalPath) {
		send(new Subscription(signalPath));
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

	public <S> Signal<S> signal(String remoteSignalPath) {
		Register<S> register = produceRegisterFor(remoteSignalPath);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

	@Override
	void crash() {
		_isCrashed = true;
	}

	void serveSubscriptionTo(String signalPath) {
		if (!signalPath.equals("Name"))
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement Auto-generated method stub

		_ownNameKeeper.name().addReceiver(_nameReceiverToAvoidGC);
	}

	void handleNotification(String signalPath, Object newValue) {
		_registersByRemotePath.get(signalPath).setter().consume(newValue);
	}

	@Override
	AbstractParty produceProxyFor(String nickname) {
		return new RemoteProxy(this, nickname);
	}

	@Override
	<S> Signal<S> signal(String signalPath, ArrayList<String> nicknamePath) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}


}
