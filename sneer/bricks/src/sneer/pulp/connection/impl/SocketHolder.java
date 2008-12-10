package sneer.pulp.connection.impl;

import sneer.pulp.network.ByteArraySocket;
import wheel.lang.Consumer;

class SocketHolder {

	private ByteArraySocket _socket;
	private final Consumer<Boolean> _activityReceiver;

	public SocketHolder(Consumer<Boolean> activityReceiver) {
		_activityReceiver = activityReceiver;
	}

	synchronized boolean isEmpty() {
		return _socket == null;
	}

	synchronized ByteArraySocket socket() {
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