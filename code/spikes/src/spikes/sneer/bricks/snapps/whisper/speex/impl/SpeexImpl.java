package spikes.sneer.bricks.snapps.whisper.speex.impl;

import spikes.sneer.bricks.snapps.whisper.speex.Decoder;
import spikes.sneer.bricks.snapps.whisper.speex.Encoder;
import spikes.sneer.bricks.snapps.whisper.speex.Speex;

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
