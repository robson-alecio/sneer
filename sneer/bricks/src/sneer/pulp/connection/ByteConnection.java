package sneer.pulp.connection;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver);
	
}