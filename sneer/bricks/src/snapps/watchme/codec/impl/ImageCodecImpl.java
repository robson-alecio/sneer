package snapps.watchme.codec.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import wheel.io.ui.graphics.Images;
import wheel.lang.exceptions.Hiccup;

class ImageCodecImpl implements ImageCodec {
	
	@Inject
	private static ImageFactory _imageFactory;

	@Override
	public List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) throws Hiccup {
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
		Image img = _imageFactory.toImage(delta.width, delta.height, delta.imageData);
		result.getGraphics().drawImage(img, delta.x, delta.y, null);
	}
}