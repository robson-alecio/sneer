package sneer.bricks.pulp.connection;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver);
	
}