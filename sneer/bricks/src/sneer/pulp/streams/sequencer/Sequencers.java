package sneer.pulp.streams.sequencer;

import sneer.brickness.Brick;
import wheel.lang.Consumer;

public interface Sequencers extends Brick {

	<T> Sequencer<T> createSequencerFor(Consumer<T> consumer,short bufferSize, short maxGap);
	
}
