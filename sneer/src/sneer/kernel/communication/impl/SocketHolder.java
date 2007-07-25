package sneer.kernel.communication.impl;

import wheel.io.Streams;
import wheel.io.network.ObjectSocket;
import wheel.lang.Omnivore;

class SocketHolder {

	private volatile ObjectSocket _socket;
	private final Omnivore<Boolean> _activityReceiver;

	public SocketHolder(Omnivore<Boolean> activityReceiver) {
		_activityReceiver = activityReceiver;
	}

	boolean isEmpty() {
		return _socket == null;
	}

	ObjectSocket socket() {
		return _socket;
	}

	synchronized void setSocketIfNecessary(ObjectSocket newSocket) {
		if (!isEmpty()) crash(newSocket);

		_socket = newSocket;
		_activityReceiver.consume(true);
	}

	synchronized void crash(ObjectSocket socket) {
		if (socket == _socket) {
			_socket = null;
			_activityReceiver.consume(false);
		}
		
		Streams.crash(socket);
	}

	synchronized void crashSocket() {
		if (_socket == null) return;
		crash(_socket);
	}

}
