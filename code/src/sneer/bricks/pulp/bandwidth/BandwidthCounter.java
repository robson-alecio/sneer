package sneer.bricks.pulp.bandwidth;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface BandwidthCounter {

	void sent(int sizeBytes);
	void received(int sizeBytes);

	Signal<Integer> downloadSpeed();
	Signal<Integer> uploadSpeed();
}