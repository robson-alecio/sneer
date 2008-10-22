package snapps.watchme.codec.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Decoder;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import wheel.lang.Pair;

class DecoderImpl implements Decoder {

	@Inject
	private static ImageFactory _imageFactory;

	private final BufferedImage _image;

	private Map<Pair<Integer, Integer>, Long> _timesByPosition = new HashMap<Pair<Integer, Integer>, Long>();

	public DecoderImpl(BufferedImage image) {
		_image = image;
	}

	@Override
	public boolean applyDelta(ImageDelta delta) {
		if (!isNew(delta)) return false;
		
		Image cell = _imageFactory.fromPngData(delta.imageData);
		_image.getGraphics().drawImage(cell, delta.x, delta.y, null);
		return true;
	}

	private boolean isNew(ImageDelta delta) {
		Pair<Integer, Integer> position = position(delta);
		Long previousTime = previousTime(position);
		if (delta.publicationTime < previousTime) return false;
		
		_timesByPosition.put(position, delta.publicationTime);
		return true;
	}

	private Long previousTime(Pair<Integer, Integer> position) {
		Long result = _timesByPosition.get(position);
		return result == null ? 0 : result;
	}

	private Pair<Integer, Integer> position(ImageDelta delta) {
		return new Pair<Integer, Integer>(delta.x, delta.y);
	}

}
