package sneer.pulp.connection;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ByteConnection {

	public interface Packet {
		byte[] payload();
	}
	
	public interface PacketScheduler {
		Packet highestPriorityPacketToSend();
		void packetWasSent(Packet packet);
	}
	
	Signal<Boolean> isOnline();

	void setSender(PacketScheduler sender);
	void setReceiver(Omnivore<byte[]> receiver);
	
}