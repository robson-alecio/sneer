package spikes.sneer.bricks.skin.audio.speaker;

import java.io.Closeable;

import javax.sound.sampled.LineUnavailableException;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface Speaker {

	public interface Line extends Consumer<byte[]>, Closeable {}

	/** Returns a line to play packets of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
	Line acquireLine() throws LineUnavailableException;

}
