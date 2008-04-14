package sneer.bricks.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Connection {

	Signal<Boolean> isOnline();

	boolean tryToSend(byte[] array);
	void setReceiver(Omnivore<byte[]> receiver);
	
}
