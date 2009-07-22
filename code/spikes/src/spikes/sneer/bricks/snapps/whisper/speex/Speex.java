package spikes.sneer.bricks.snapps.whisper.speex;

import sneer.foundation.brickness.Brick;

@Brick
public interface Speex {

	Encoder createEncoder();

	Decoder createDecoder();

}
