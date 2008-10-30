package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void setSender(PacketScheduler sender);
	void setReceiver(Omnivore<byte[]> receiver);
	
}