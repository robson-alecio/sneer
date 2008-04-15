package sneer.bricks.connection.impl;

import sneer.bricks.network.ByteArraySocket;
import wheel.lang.Omnivore;

class SocketHolder {

	private volatile ByteArraySocket _socket;
	private final Omnivore<Boolean> _activityReceiver;

	public SocketHolder(Omnivore<Boolean> activityReceiver) {
		_activityReceiver = activityReceiver;
	}

	synchronized boolean isEmpty() {
		return _socket == null;
	}

	ByteArraySocket socket() {
		return _socket;
	}

	synchronized void setSocketIfNecessary(ByteArraySocket newSocket) {
		if (!isEmpty()) {
			newSocket.crash();
			return;
		}

		_socket = newSocket;
		_activityReceiver.consume(true);
	}

	synchronized void crash(ByteArraySocket referenceToSocket) {
		referenceToSocket.crash();

		if (referenceToSocket != _socket) return;
		_socket = null;
		_activityReceiver.consume(false);
	}

	synchronized void crashSocket() {
		if (_socket == null) return;
		crash(_socket);
	}

}
