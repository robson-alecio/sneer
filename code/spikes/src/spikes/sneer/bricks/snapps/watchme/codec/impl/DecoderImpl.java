package spikes.sneer.bricks.snapps.watchme.codec.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.skin.image.ImageFactory;
import sneer.foundation.lang.Pair;
import spikes.sneer.bricks.snapps.watchme.codec.ImageDelta;
import spikes.sneer.bricks.snapps.watchme.codec.ImageCodec.Decoder;

class DecoderImpl implements Decoder {

	private final ImageFactory _imageFactory = my(ImageFactory.class);

	private BufferedImage _image;

	private Map<Pair<Integer, Integer>, Long> _timesByPosition = new HashMap<Pair<Integer, Integer>, Long>();

	public DecoderImpl() {
	}	
	
	public DecoderImpl(BufferedImage image) {
		_image = image;
	}

	@Override
	public boolean applyDelta(ImageDelta delta) {
		if (!isNew(delta)) return false;
		
		Image cell = _imageFactory.fromPngData(delta.imageData.copy());
		
		if (_image == null) _image = generateBlankImage(1024, 768);
		
		_image.getGraphics().drawImage(cell, delta.x, delta.y, null);
		return true;
	}
	
	@Override
	public BufferedImage screen() {
		return _image;
	}

	private boolean isNew(ImageDelta delta) {
		Pair<Integer, Integer> position = position(delta);
		Long previousTime = previousTime(position);
		if (delta.publicationTime() < previousTime) return false;
		
		_timesByPosition.put(position, delta.publicationTime());
		return true;
	}

	private Long previousTime(Pair<Integer, Integer> position) {
		Long result = _timesByPosition.get(position);
		return result == null ? 0 : result;
	}

	private Pair<Integer, Integer> position(ImageDelta delta) {
		return new Pair<Integer, Integer>(delta.x, delta.y);
	}
	
	private BufferedImage generateBlankImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

}
