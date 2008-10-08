package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import wheel.io.ui.graphics.Images;

class ImageCodecImpl implements ImageCodec {

	static final int CELL_SIZE = 64;

	@Override
	@Ignore
	public BufferedImage decodeDeltas(BufferedImage original,	Iterable<ImageDelta> deltas) {
		BufferedImage result = Images.copy(original);
		for (ImageDelta delta : deltas) {
			applyDelta(result, delta);
		}
		return result;
	}

	private void applyDelta(BufferedImage result, ImageDelta delta) {
		result.getGraphics().drawImage(delta.imageCell(), delta.x, delta.y, null);
	}

	static class SplitCounter{
		int x;
		int y;
		int xRemainder;
		int yRemainder;
		
		SplitCounter(BufferedImage image){
			x = image.getWidth()/CELL_SIZE;
			y = image.getHeight()/CELL_SIZE;
			xRemainder = image.getWidth() % CELL_SIZE;
			yRemainder = image.getHeight() % CELL_SIZE;
		}
	}
	
	@Override
	public List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) {
		List<ImageDelta> result = new ArrayList<ImageDelta>();
		SplitCounter counter = new SplitCounter(original);
		splitImage(original, target, result, counter);
		return result;
	}

	private void splitImage(BufferedImage original, BufferedImage target,	List<ImageDelta> result, SplitCounter counter) {
		for (int i = 0; i < counter.x; i++) 
			for (int j = 0; j < counter.y; j++) 
				tryAddImageDelta(original, target, result, i*CELL_SIZE, j*CELL_SIZE);
	}

	private void tryAddImageDelta(BufferedImage original, BufferedImage target, List<ImageDelta> result, int x, int y) {
		BufferedImage img0 = original.getSubimage(x, y, CELL_SIZE, CELL_SIZE);
		BufferedImage img1 = target.getSubimage(x, y, CELL_SIZE, CELL_SIZE);
		if(!Images.isSameImage(img0, img1))
			try {
				result.add(new ImageDelta(img1,x,y));
			} catch (InterruptedException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
	}
}