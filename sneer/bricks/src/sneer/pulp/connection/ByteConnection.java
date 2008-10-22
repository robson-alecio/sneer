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

	void legacySend(byte[] packet); //Refactor: delete
	void setLegacyReceiver(Omnivore<byte[]> receiver); //Refactor: delete

	void setSender(PacketScheduler sender);
	void setReceiver(Omnivore<byte[]> receiver);
	
}