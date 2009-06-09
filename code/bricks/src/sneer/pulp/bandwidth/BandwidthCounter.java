package sneer.pulp.bandwidth;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface BandwidthCounter {

	void sent(int sizeBytes);
	void received(int sizeBytes);

	Signal<Integer> downloadSpeed();
	Signal<Integer> uploadSpeed();
}