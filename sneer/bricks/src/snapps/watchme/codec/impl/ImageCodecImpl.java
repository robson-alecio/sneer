package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import wheel.io.ui.graphics.Images;

class ImageCodecImpl implements ImageCodec {

	@Override
	public BufferedImage decodeDeltas(BufferedImage original,	Iterable<ImageDelta> deltas) {
		return deltas.iterator().next().target;
	}

	@Override
	public Iterable<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) {
		List<ImageDelta> result = new ArrayList<ImageDelta>();
		if (!Images.isSameImage(original, target))
			result.add(new ImageDelta(target));
		return result;
	}

}