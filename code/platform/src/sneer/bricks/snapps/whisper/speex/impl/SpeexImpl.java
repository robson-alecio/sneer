package sneer.bricks.snapps.whisper.speex.impl;

import sneer.bricks.snapps.whisper.speex.Decoder;
import sneer.bricks.snapps.whisper.speex.Encoder;
import sneer.bricks.snapps.whisper.speex.Speex;

class SpeexImpl implements Speex {

	@Override
	public Decoder createDecoder() {
		return new DecoderImpl();
	}

	@Override
	public Encoder createEncoder() {
		return new EncoderImpl();
	}

}
