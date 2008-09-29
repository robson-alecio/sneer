package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	Signal<Boolean> isOnline();

	void legacySend(byte[] array);
	void setLegacyReceiver(Omnivore<byte[]> receiver); //Refactor
	
	
}
