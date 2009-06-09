package sneer.bricks.pulp.streams.sequencer.impl;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.streams.sequencer.Sequencer;
import sneer.bricks.pulp.streams.sequencer.Sequencers;

class SequencersImpl implements Sequencers {

	@Override
	public <T> Sequencer<T> createSequencerFor(Consumer<T> consumer, short bufferSize, short maxGap) {
		return new SequencerImpl<T>(consumer, bufferSize, maxGap);
	}

}