package sneer.pulp.connection.impl;

import sneer.pulp.network.ByteArraySocket;
import wheel.io.Logger;
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
			Logger.log("New socket crashed: " + newSocket);
			newSocket.crash();
			return;
		}

		Logger.log("New socket accepted: " + newSocket);

		_socket = newSocket;
		_activityReceiver.consume(true);
	}

	synchronized void crash(ByteArraySocket referenceToSocket) {
		referenceToSocket.crash();

		if (referenceToSocket != _socket) {
			Logger.log("Crashing different socket: " + referenceToSocket);
			return;
		}

		Logger.log("Crashing socket: ", _socket);

		_socket = null;
		_activityReceiver.consume(false);
	}

	synchronized void crashSocket() {
		if (_socket == null) return;
		crash(_socket);
	}
}