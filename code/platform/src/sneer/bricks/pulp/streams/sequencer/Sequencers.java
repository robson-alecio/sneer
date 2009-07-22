package sneer.bricks.pulp.streams.sequencer;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface Sequencers {

	<T> Sequencer<T> createSequencerFor(short bufferSize,short maxGap, Consumer<T> consumer);
	
}
