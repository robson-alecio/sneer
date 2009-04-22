package sneer.pulp.streams.sequencer;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;

@Brick
public interface Sequencers {

	<T> Sequencer<T> createSequencerFor(Consumer<T> consumer,short bufferSize, short maxGap);
	
}
