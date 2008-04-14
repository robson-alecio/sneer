package sneer.bricks.mesh.impl;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Peer;
import sneer.bricks.serialization.Serializer;
import sneer.lego.Crashable;
import sneer.lego.Inject;
import sneer.lego.Injector;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class PeerImpl implements Peer, Crashable {

	@Inject
	private ConnectionManager _connectionManager;
	
	private final Map<String, Register<?>> _registersByPath = new HashMap<String, Register<?>>();

	private final Connection _connection;

	@Inject
	private Serializer _serializer;


	PeerImpl(Injector injector, Contact contact) {
		injector.inject(this);
		_connection = _connectionManager.connectionFor(contact);
		_connection.setReceiver(new Omnivore<byte[]>(){public void consume(byte[] packetReceived) {
			// Implement Auto-generated method stub
			throw new wheel.lang.exceptions.NotImplementedYet();
		}});
	}

	private <T> Register<T> produceRegisterFor(String signalPath) {
		Register<?> register = _registersByPath.get(signalPath);
		if (register == null) {
			subscribeTo(signalPath); 
			register = new RegisterImpl<Object>(null);
			_registersByPath.put(signalPath, register);
		}
		return Casts.uncheckedGenericCast(register);
	}

	private void subscribeTo(String signalPath) {
		send(new SignalSubscription(signalPath));
	}

	private void send(Object object) {
		byte[] serialized = serialize(object);

		if (!_connection.tryToSend(serialized));
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	private byte[] serialize(Object object) {
		try {
			return _serializer.serialize(object);
		} catch (NotSerializableException e) {
			throw new RuntimeException(e);
		}
	}

	public <S> Signal<S> signal(String signalPath) {
		Register<S> register = produceRegisterFor(signalPath);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

	public void crash() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}


}
