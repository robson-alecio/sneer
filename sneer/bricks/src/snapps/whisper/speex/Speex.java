package snapps.whisper.speex;

import sneer.brickness.Brick;

public interface Speex extends Brick {
	Encoder createEncoder();
	Decoder createDecoder();
}
