package spikes.sneer.bricks.snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;

import spikes.sneer.bricks.snapps.watchme.codec.ImageCodec;

class ImageCodecImpl implements ImageCodec {
	
	@Override
	public Encoder createEncoder() {
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