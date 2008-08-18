package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	Signal<Boolean> isOnline();

	void send(byte[] array);
	void setReceiver(Omnivore<byte[]> receiver);
	
}
