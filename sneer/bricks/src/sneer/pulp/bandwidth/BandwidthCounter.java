package sneer.pulp.bandwidth;

import sneer.kernel.container.Brick;
import wheel.reactive.Signal;

public interface BandwidthCounter extends Brick {

	void packetSended(int sizeBytes);
	void packetReceived(int sizeBytes);

	Signal<Integer> downloadSpeed();
	Signal<Integer> uploadSpeed();
}