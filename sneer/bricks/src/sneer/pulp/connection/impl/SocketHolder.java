package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.log.Logger;
import sneer.pulp.network.ByteArraySocket;


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
			my(Logger.class).log("New socket crashed: " + newSocket);
			newSocket.crash();
			return;
		}

		my(Logger.class).log("New socket accepted: " + newSocket);

		_socket = newSocket;
		_activityReceiver.consume(true);
	}

	synchronized void crash(ByteArraySocket referenceToSocket) {
		referenceToSocket.crash();

		if (referenceToSocket != _socket) {
			my(Logger.class).log("Crashing different socket: " + referenceToSocket);
			return;
		}

		my(Logger.class).log("Crashing socket: ", _socket);

		_socket = null;
		_activityReceiver.consume(false);
	}

	synchronized void crashSocket() {
		if (_socket == null) return;
		crash(_socket);
	}
}