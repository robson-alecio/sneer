package sneer.bricks.pulp.streams.sequencer.impl;

import sneer.bricks.pulp.streams.sequencer.Sequencer;
import sneer.bricks.pulp.streams.sequencer.Sequencers;
import sneer.foundation.lang.Consumer;

class SequencersImpl implements Sequencers {

	@Override
	public <T> Sequencer<T> createSequencerFor(short bufferSize, short maxGap, Consumer<T> consumer) {
		return new SequencerImpl<T>(consumer, bufferSize, maxGap);
	}

}