/**
 * 
 */
package snapps.watchme.codec.impl;

import static snapps.watchme.codec.impl.CodecConstants.CELL_SIZE;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import snapps.watchme.codec.ImageDelta;
import wheel.io.ui.graphics.Images;

class EncoderStep{
	private final BufferedImage _original;
	private final BufferedImage _target;
	private final int _widthInCells;
	private final int _heightInCells;
	private final int _widthRemainderPixels;
	private final int _heightRemainderPixels;
	private final List<ImageDelta> _result;
	
	EncoderStep(BufferedImage original, BufferedImage target){
		_original = original;
		_target = target;
		_widthRemainderPixels = original.getWidth() % CELL_SIZE;
		_heightRemainderPixels = original.getHeight() % CELL_SIZE;
		_widthInCells = original.getWidth() / CELL_SIZE;
		_heightInCells = original.getHeight() / CELL_SIZE;
		_result = new ArrayList<ImageDelta>();
		produceResult();
	}
	
	public List<ImageDelta> result() {
		return _result;
	}

	private void produceResult() {
		for (int x = 0; x < _original.getWidth(); x = x + CELL_SIZE) 
			for (int y = 0; y < _original.getHeight(); y = y + CELL_SIZE) 
				addImageDeltaIfNecessary(x, y);
		
		
	}
	
	private void addImageDeltaIfNecessary(int x, int y) {
		int width = Math.min(CELL_SIZE, _original.getWidth() - x);
		int height = Math.min(CELL_SIZE, _original.getHeight() - y);
		BufferedImage img0 = _original.getSubimage(x, y, width, height);
		BufferedImage img1 = _target.getSubimage(x, y, width, height);
		if(!Images.isSameImage(img0, img1))
			try {
				_result.add(new ImageDelta(img1,x,y));
			} catch (InterruptedException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
	}
}