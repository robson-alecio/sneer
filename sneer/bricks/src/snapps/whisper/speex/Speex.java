package snapps.whisper.speex;

import sneer.brickness.OldBrick;

public interface Speex extends OldBrick {
	Encoder createEncoder();
	Decoder createDecoder();
}
