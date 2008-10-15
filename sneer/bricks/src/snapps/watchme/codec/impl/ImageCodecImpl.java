package snapps.watchme.codec.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import wheel.lang.exceptions.Hiccup;

class ImageCodecImpl implements ImageCodec {
	
	@Inject
	private static ImageFactory _imageFactory;

	@Override
	public List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) throws Hiccup {
		return new EncoderStep(original, target).result();
	}

	@Override
	public void applyDelta(BufferedImage image, ImageDelta delta) {
		Image cell = _imageFactory.fromPngData(delta.imageData);
		image.getGraphics().drawImage(cell, delta.x, delta.y, null);
	}
}