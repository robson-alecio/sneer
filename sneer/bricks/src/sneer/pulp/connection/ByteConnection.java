package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void lastRequestedPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void legacySend(byte[] packet); //Refactor: delete
	void setLegacyReceiver(Omnivore<byte[]> receiver); //Refactor: delete

	void setSender(PacketScheduler sender);
	void setReceiver(Omnivore<byte[]> receiver);
	
}