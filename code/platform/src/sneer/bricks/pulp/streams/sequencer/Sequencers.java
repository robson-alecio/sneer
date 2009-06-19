package sneer.bricks.pulp.streams.sequencer;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface Sequencers {

	<T> Sequencer<T> createSequencerFor(Consumer<T> consumer,short bufferSize, short maxGap);
	
}
