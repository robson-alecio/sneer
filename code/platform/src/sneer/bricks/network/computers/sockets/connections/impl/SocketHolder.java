package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;


class SocketHolder {

	private final Register<Boolean> _isConnected = my(Signals.class).newRegister(false);

	private ByteArraySocket _socket;
	
	
	synchronized
	ByteArraySocket socket() {
		return _socket;
	}

	
	synchronized
	void setSocket(ByteArraySocket newSocket) {
		if (_socket != null) throw new IllegalStateException();
		_socket = newSocket;

		_isConnected.setter().consume(true);
		notifyAll();
		
		my(Logger.class).log("New socket set.");
	}

	
	synchronized
	void overrideSocket(ByteArraySocket newSocket) {
		close("Existing socket overriden by new socket.");
		setSocket(newSocket);
	}
	
	
	synchronized
	ByteArraySocket waitForSocket() {
		while (_socket == null)
			my(Threads.class).waitWithoutInterruptions(this);
		
		return _socket;
	}

	
	synchronized
	void close(ByteArraySocket socket, String message, IOException exception) {
		SocketCloser.close(socket, message, exception);
		if (socket != _socket) return;
		
		_socket = null;
		_isConnected.setter().consume(false);
	}

	
	synchronized
	void close(String message) {
		if (_socket == null) return;
		SocketCloser.close(_socket, message);
		_socket = null;
	}


	Signal<Boolean> isConnected() {
		return _isConnected.output();
	}
	
}