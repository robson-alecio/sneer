package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.contacts.Contact;
import sneer.lego.Brick;
import sneer.lego.Injector;
import wheel.lang.Pair;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class ContactProxy {

	@Brick
	private ConnectionManager _connectionManager;
	
	private final Map<Pair<String, String>, Register<?>> _registersByPath = new HashMap<Pair<String, String>, Register<?>>();

	private final Connection _connection;


	public ContactProxy(Injector injector, Contact contact) {
		injector.inject(this);
		_connection = _connectionManager.connectionFor(contact); 
	}

	public <T> Signal<T> findSignal(String nicknamePath, String signalPath) {
		Register<T> register = produceRegisterFor(nicknamePath, signalPath);
		return register.output();
	}

	private <T> Register<T> produceRegisterFor(String nicknamePath, String signalPath) {
		Pair<String, String> fullpath = new Pair<String, String>(nicknamePath, signalPath);
		Register<T> register = (Register<T>)_registersByPath.get(fullpath);
		if (register == null) {
			subscribeTo(nicknamePath, signalPath);
			register = new RegisterImpl<T>(null);
			_registersByPath.put(fullpath, register);
		}
		return register;
	}

	private void subscribeTo(String nicknamePath, String signalPath) {
//		_connection.send(new SignalSubscription(nicknamePath, signalPath));
	}

}
