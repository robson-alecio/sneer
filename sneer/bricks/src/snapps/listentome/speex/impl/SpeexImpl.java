package snapps.listentome.speex.impl;

import snapps.listentome.speex.Decoder;
import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;

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
