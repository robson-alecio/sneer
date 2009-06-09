package sneer.pulp.connection;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

public interface ByteConnection {

	public interface PacketScheduler {
		byte[] highestPriorityPacketToSend();
		void previousPacketWasSent();
	}
	
	Signal<Boolean> isOnline();

	void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver);
	
}