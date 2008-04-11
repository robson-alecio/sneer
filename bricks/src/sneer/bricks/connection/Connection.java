package sneer.bricks.connection;

import java.io.IOException;

import wheel.reactive.Signal;

public interface Connection {

	Signal<Boolean> isOnline();

	void send(byte[] array) throws IOException;
	
}
