/**
 * 
 */
package snapps.watchme.codec.impl;

import static snapps.watchme.codec.impl.CodecConstants.CELL_SIZE;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import wheel.io.ui.graphics.Images;
import wheel.lang.exceptions.Hiccup;

class EncoderStep{
	private final BufferedImage _original;
	private final BufferedImage _target;
	private final List<ImageDelta> _result;

	@Inject
	private static ImageFactory _imageFactory;
	
	EncoderStep(BufferedImage original, BufferedImage target) throws Hiccup {
		_original = original;
		_target = target;
		_result = new ArrayList<ImageDelta>();
		produceResult();
	}
	
	public List<ImageDelta> result() {
		return _result;
	}

	private void produceResult() throws Hiccup {
		for (int x = 0; x < _original.getWidth(); x = x + CELL_SIZE) 
			for (int y = 0; y < _original.getHeight(); y = y + CELL_SIZE) 
				addImageDeltaIfNecessary(x, y);
	}
	
	private void addImageDeltaIfNecessary(int x, int y) throws Hiccup {
		int width = Math.min(CELL_SIZE, _original.getWidth() - x);
		int height = Math.min(CELL_SIZE, _original.getHeight() - y);
		BufferedImage img0 = _original.getSubimage(x, y, width, height);
		BufferedImage img1 = _target.getSubimage(x, y, width, height);
		if(Images.isSameImage(img0, img1)) return;
		
		int[] data = _imageFactory.toSerializableData(img1);
		_result.add(new ImageDelta(data,x,y, width, height));
	}
}