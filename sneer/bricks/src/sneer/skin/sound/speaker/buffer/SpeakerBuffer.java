package sneer.skin.sound.speaker.buffer;

import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Omnivore;

public interface SpeakerBuffer extends Omnivore<PcmSoundPacket>{

	void crash();

}
