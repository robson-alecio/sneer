package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.lang.Closure;

class SocketCloser {

	static void closeIfUnsuccessful(ByteArraySocket socket,	String message, Closure<IOException> closure) {
		try {
			closure.run();
		} catch (IOException e) {
			close(socket, message, e);
		}
	}

	
	static void close(ByteArraySocket socket, String message, IOException e) {
		close(socket, message + " " + e.getMessage());
	}

	
	static void close(ByteArraySocket socket, String message) {
		socket.close();
		my(Logger.class).log(message);
	}

}
