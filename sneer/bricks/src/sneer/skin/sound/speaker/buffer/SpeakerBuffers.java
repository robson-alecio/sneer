package sneer.skin.sound.speaker.buffer;

import sneer.kernel.container.Brick;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Omnivore;

public interface SpeakerBuffers extends Brick {

	Omnivore<PcmSoundPacket> createBufferFor(Omnivore<? super PcmSoundPacket> consumer);
	
}
