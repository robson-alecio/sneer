package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import wheel.io.ui.graphics.Images;

class ImageCodecImpl implements ImageCodec {

	@Override
	public List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) {
		return new EncoderStep(original, target).result();
	}

	@Override
	public BufferedImage decodeDeltas(BufferedImage original,	Iterable<ImageDelta> deltas) {
		BufferedImage result = Images.copy(original);
		for (ImageDelta delta : deltas)
			applyDelta(result, delta);
		
		return result;
	}

	private void applyDelta(BufferedImage result, ImageDelta delta) {
		result.getGraphics().drawImage(delta.imageCell(), delta.x, delta.y, null);
	}
	
}