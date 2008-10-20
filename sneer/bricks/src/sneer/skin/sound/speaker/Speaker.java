package sneer.skin.sound.speaker;

import javax.sound.sampled.LineUnavailableException;

import sneer.kernel.container.Brick;

public interface Speaker extends Brick {

	/** Will start playing PcmSoundPacket tuples with duration of one hundredth of a second each. 
	 * @throws LineUnavailableException */
	void open();
	void close();
}
