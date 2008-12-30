package snapps.whisper.speex;

import sneer.kernel.container.Brick;

public interface Speex extends Brick {
	Encoder createEncoder();
	Decoder createDecoder();
}
