package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import wheel.lang.exceptions.Hiccup;

class ImageCodecImpl implements ImageCodec {
	
	@Override
	public List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) throws Hiccup {
		return new EncoderStep(original, target).result();
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