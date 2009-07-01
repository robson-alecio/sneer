package sneer.bricks.network.computers.sockets.connections;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.Consumer;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isConnected();

	void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver);

	
}