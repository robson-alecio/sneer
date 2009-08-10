package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.lang.Consumer;


class SocketHolder {

	private ByteArraySocket _socket;
	private final Consumer<Boolean> _hasSocketReceiver;

	
	SocketHolder(Consumer<Boolean> hasSocketReceiver) {
		_hasSocketReceiver = hasSocketReceiver;
	}

	
	synchronized
	ByteArraySocket socket() {
		return _socket;
	}

	
	synchronized
	void setSocket(ByteArraySocket newSocket) {
		if (_socket != null) throw new IllegalStateException();
		_socket = newSocket;

		_hasSocketReceiver.consume(true);
		notifyAll();
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
		if (_socket == socket) _socket = null;
	}

	
	synchronized
	void close(String message) {
		if (_socket == null) return;
		SocketCloser.close(_socket, message);
		_socket = null;
	}

}