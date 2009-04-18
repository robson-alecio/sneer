package sneer.pulp.streams.sequencer.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.streams.sequencer.Sequencer;
import sneer.pulp.streams.sequencer.Sequencers;

class SequencersImpl implements Sequencers {

	@Override
	public <T> Sequencer<T> createSequencerFor(Consumer<T> consumer, short bufferSize, short maxGap) {
		return new SequencerImpl<T>(consumer, bufferSize, maxGap);
	}

}