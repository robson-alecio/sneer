package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	public interface Sender {
		byte[] currentPacketToSend();
		void currentPacketSent();

	}
	Signal<Boolean> isOnline();

	void legacySend(byte[] packet); //Refactor: delete
	void setLegacyReceiver(Omnivore<byte[]> receiver); //Refactor: delete

	void setSender(Sender sender);
	void setReceiver(Omnivore<byte[]> receiver);
	
}