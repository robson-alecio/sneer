package snapps.watchme.codec.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;

import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Decoder;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;

class DecoderImpl implements Decoder {

	@Inject
	private static ImageFactory _imageFactory;

	private final BufferedImage _image;

	public DecoderImpl(BufferedImage image) {
		_image = image;
	}

	@Override
	public boolean applyDelta(ImageDelta delta) {
		Image cell = _imageFactory.fromPngData(delta.imageData);
		_image.getGraphics().drawImage(cell, delta.x, delta.y, null);
		return true;
	}

}
