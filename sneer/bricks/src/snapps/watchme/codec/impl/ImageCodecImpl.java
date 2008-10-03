package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;

class ImageCodecImpl implements ImageCodec {

	@Override
	public BufferedImage decodeDeltas(BufferedImage imageA,
			Iterable<ImageDelta> deltas) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Iterable<ImageDelta> encodeDeltas(BufferedImage imageA,
			BufferedImage imageB) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}