package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.lang.Consumer;


class SocketHolder {

	private ByteArraySocket _socket;
	private final Consumer<Boolean> _activityReceiver;

	
	SocketHolder(Consumer<Boolean> activityReceiver) {
		_activityReceiver = activityReceiver;
	}

	
	synchronized
	boolean isEmpty() {
		return _socket == null;
	}

	
	synchronized
	ByteArraySocket socket() {
		return _socket;
	}

	
	synchronized
	boolean setSocketIfNecessary(ByteArraySocket newSocket) {
		if (!isEmpty()) {
			my(Logger.class).log("New socket crashed: ", newSocket);
			newSocket.crash();
			return false;
		}

		_socket = newSocket;
		_activityReceiver.consume(true);
		notifyAll();
		return true;
	}

	
	synchronized
	void crash(ByteArraySocket referenceToSocket) {
		referenceToSocket.crash();

		if (referenceToSocket != _socket) {
			my(Logger.class).log("Crashing different socket: ", referenceToSocket);
			return;
		}

		my(Logger.class).log("Crashing socket: ", _socket);

		_socket = null;
		_activityReceiver.consume(false);
	}


	synchronized
	ByteArraySocket waitForSocket() {
		while (_socket == null)
			my(Threads.class).waitWithoutInterruptions(this);
		
		return _socket;
	}

}