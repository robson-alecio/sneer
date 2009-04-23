package snapps.whisper.speex;

import sneer.brickness.Brick;

@Brick
public interface Speex {

	Encoder createEncoder();

	Decoder createDecoder();

}
