package sneer.skin.sound.speaker.buffer.impl;

import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.speaker.buffer.SpeakerBuffer;
import sneer.skin.sound.speaker.buffer.SpeakerBuffers;
import wheel.lang.Omnivore;

class SpeakerBuffersImpl implements SpeakerBuffers {

	@Override
	public SpeakerBuffer createBufferFor(Omnivore<? super PcmSoundPacket> consumer) {
		return new SpeakerBufferImpl(consumer);
	}

}