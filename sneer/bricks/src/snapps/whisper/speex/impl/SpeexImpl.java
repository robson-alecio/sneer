package snapps.whisper.speex.impl;

import snapps.whisper.speex.Decoder;
import snapps.whisper.speex.Encoder;
import snapps.whisper.speex.Speex;

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
