package sneer.pulp.streams.sequencer.impl;

import sneer.pulp.streams.sequencer.Sequencer;
import sneer.pulp.streams.sequencer.Sequencers;
import wheel.lang.Consumer;

class SequencersImpl implements Sequencers {

	@Override
	public <T> Sequencer<T> createSequencerFor(Consumer<T> consumer, short bufferSize, short maxGap) {
		return new SequencerImpl<T>(consumer, bufferSize, maxGap);
	}

}