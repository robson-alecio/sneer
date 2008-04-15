package sneer.bricks.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Connection {

	Signal<Boolean> isOnline();

	void send(byte[] array);
	void setReceiver(Omnivore<byte[]> receiver);
	
}
