package sneer.pulp.streams.sequencer.impl;

import sneer.pulp.streams.sequencer.Sequencer;
import sneer.pulp.streams.sequencer.Sequencers;
import sneer.software.lang.Consumer;

class SequencersImpl implements Sequencers {

	@Override
	public <T> Sequencer<T> createSequencerFor(Consumer<T> consumer, short bufferSize, short maxGap) {
		return new SequencerImpl<T>(consumer, bufferSize, maxGap);
	}

}