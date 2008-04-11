package sneer.bricks.mesh.impl;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Peer;
import sneer.bricks.serialization.Serializer;
import sneer.lego.Inject;
import sneer.lego.Injector;
import wheel.lang.Pair;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class PeerImpl implements Peer {

	@Inject
	private ConnectionManager _connectionManager;
	
	private final Map<String, Register<?>> _registersByPath = new HashMap<String, Register<?>>();

	private final Connection _connection;

	@Inject
	private Serializer _serializer;


	PeerImpl(Injector injector, Contact contact) {
		injector.inject(this);
		_connection = _connectionManager.connectionFor(contact); 
	}

	private <T> Register<T> produceRegisterFor(String signalPath) {
		Register<?> register = _registersByPath.get(signalPath);
		if (register == null) {
			subscribeTo(signalPath); 
			register = new RegisterImpl<Object>(null);
			_registersByPath.put(signalPath, register);
		}
		return (Register<T>)register;
	}

	private void subscribeTo(String signalPath) {
		send(new SignalSubscription(signalPath));
	}

	private void send(Object object) {
		byte[] serialized;
		try {
			serialized = _serializer.serialize(object);
		} catch (NotSerializableException e) {
			throw new RuntimeException(e);
		}
		_connection.send(serialized);
	}

	public Signal<?> signal(String signalPath) {
		Register<?> register = produceRegisterFor(signalPath);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

}
