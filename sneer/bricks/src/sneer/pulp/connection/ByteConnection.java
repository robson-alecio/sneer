package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	Signal<Boolean> isOnline();

	void legacySend(byte[] packet); //Refactor: delete
	void setLegacyReceiver(Omnivore<byte[]> receiver); //Refactor: delete

	void send(byte[] packet);
	void setReceiver(Omnivore<byte[]> receiver);
	
}
