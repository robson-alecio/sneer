package sneer.skin.sound.speaker.buffer.impl;

import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Omnivore;


class SpeakerBufferImpl implements Omnivore<PcmSoundPacket> {

	private Omnivore<PcmSoundPacket> _consumer;


	@Override
	public synchronized void consume(PcmSoundPacket packet) {
		_consumer.consume(packet);
	}

	
}
