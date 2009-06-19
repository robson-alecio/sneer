package sneer.bricks.pulp.connection;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.Consumer;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver);
	
}