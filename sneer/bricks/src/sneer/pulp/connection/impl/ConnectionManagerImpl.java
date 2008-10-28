package sneer.pulp.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threadpool.Stepper;

class ConnectionManagerImpl implements ConnectionManager {

	private static final int THIRTY_SECONDS = 30*1000;

	@Inject
	private static OwnNameKeeper _nameKeeper;
	
	@Inject
	private static Clock _clock;
	{
		_clock.wakeUpEvery(THIRTY_SECONDS, new Stepper() { @Override public boolean step() {
			if (_clock.time() - _lastIncomingSocketTime >= THIRTY_SECONDS)
				_lights.turnOnIfNecessary(unreachableLight(), "Unreachable", "You have not received any incoming socket connections recently. Either none of your contacts are online or your machine is unreachable from the internet.");
			return true;
		}});
	}
	
	@Inject
	private static BlinkingLights _lights;
	
	private static Light _unreachable;

	private final Map<Contact, ByteConnection> _connectionsByContact = new HashMap<Contact, ByteConnection>();

	private long _lastIncomingSocketTime = _clock.time();
	
	@Override
	public synchronized ByteConnectionImpl connectionFor(Contact contact) {
		ByteConnectionImpl result = (ByteConnectionImpl) _connectionsByContact.get(contact);
		if (result == null) {
			result = new ByteConnectionImpl("" + _nameKeeper.name(), contact);
			_connectionsByContact.put(contact, result);
		}
		return result;
	}

	@Override
	public void manageIncomingSocket(Contact contact, ByteArraySocket socket) {
		updateIncomingSocketTime();
		ByteConnectionImpl connection = connectionFor(contact);
		connection.manageIncomingSocket(socket);
	}

	@Override
	public void manageOutgoingSocket(Contact contact, ByteArraySocket socket) {
		ByteConnectionImpl connection = connectionFor(
				contact);
		connection.manageOutgoingSocket(socket);
	}
	
	private void updateIncomingSocketTime() {
		_lastIncomingSocketTime = _clock.time();
		_lights.turnOffIfNecessary(unreachableLight());
	}

	private synchronized static Light unreachableLight() {
		if (null == _unreachable)
			_unreachable = _lights.prepare(LightType.WARN);
		return _unreachable;
	}
}
