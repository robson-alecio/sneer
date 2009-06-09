package sneer.bricks.pulp.streams.sequencer;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.foundation.brickness.Brick;

@Brick
public interface Sequencers {

	<T> Sequencer<T> createSequencerFor(Consumer<T> consumer,short bufferSize, short maxGap);
	
}
