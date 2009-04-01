package sneer.pulp.bandwidth;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;

public interface BandwidthCounter extends OldBrick {

	void sent(int sizeBytes);
	void received(int sizeBytes);

	Signal<Integer> downloadSpeed();
	Signal<Integer> uploadSpeed();
}