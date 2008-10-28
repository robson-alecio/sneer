package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;

import snapps.watchme.codec.ImageCodec;
import wheel.lang.exceptions.Hiccup;

class ImageCodecImpl implements ImageCodec {
	
	/**
	 * @throws Hiccup  
	 */
	@Override
	public Encoder createEncoder() throws Hiccup {
		return new EncoderImpl();
	}

	@Override
	public Decoder createDecoder() {
		return new DecoderImpl();
	}
	
	@Override
	public Decoder createDecoder(BufferedImage image) {
		return new DecoderImpl(image);
	}
	
}