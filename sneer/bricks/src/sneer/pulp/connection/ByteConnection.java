package sneer.pulp.connection;

import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver);
	
}