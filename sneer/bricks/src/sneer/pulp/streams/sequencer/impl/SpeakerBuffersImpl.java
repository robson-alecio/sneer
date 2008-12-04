package sneer.pulp.streams.sequencer.impl;

import sneer.pulp.streams.sequencer.SpeakerBuffer;
import sneer.pulp.streams.sequencer.SpeakerBuffers;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Consumer;

class SpeakerBuffersImpl implements SpeakerBuffers {

	@Override
	public SpeakerBuffer createBufferFor(Consumer<? super PcmSoundPacket> consumer) {
		return new SpeakerBufferImpl(consumer);
	}

}