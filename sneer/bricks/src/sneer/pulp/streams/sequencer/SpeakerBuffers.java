package sneer.pulp.streams.sequencer;

import sneer.kernel.container.Brick;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Consumer;

public interface SpeakerBuffers extends Brick {

	SpeakerBuffer createBufferFor(Consumer<? super PcmSoundPacket> consumer);
	
}
