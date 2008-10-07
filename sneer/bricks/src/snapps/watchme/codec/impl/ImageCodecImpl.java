package snapps.watchme.codec.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import wheel.io.ui.graphics.Images;

class ImageCodecImpl implements ImageCodec {

	static final int DEFAULT_CHUNK_SIZE = 64;

	@Override
	public BufferedImage decodeDeltas(BufferedImage original,	Iterable<ImageDelta> deltas) {
		return deltas.iterator().next()._targetImage;
	}

	@Override
	public Iterable<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) {
		List<ImageDelta> result = new ArrayList<ImageDelta>();
		
		if(hasSameSize(original, target)){
			result.add(new ImageDelta(target));
			return result;
		}
		
		SplitCounter counter = new SplitCounter(original);
		splitImage(original, target, result, counter);
		return result;
	}

	private void splitImage(BufferedImage original, BufferedImage target,	List<ImageDelta> result, SplitCounter counter) {
		for (int i = 0; i < counter.x; i++) {
			for (int j = 0; j < counter.y; j++) {
				tryAddImageDelta(original, target, result, i, j);
			}
		}
	}

	private boolean hasSameSize(BufferedImage original, BufferedImage target) {
		return original.getHeight()!=target.getHeight()
			 || original.getWidth()!=target.getWidth();
	}

	private void tryAddImageDelta(BufferedImage original, BufferedImage target, List<ImageDelta> result, int x, int y) {
		tryAddImageDelta(original, target, result, x, y, 64, 64);
	}
	
	private void tryAddImageDelta(BufferedImage original, BufferedImage target, List<ImageDelta> result, int x, int y, int width, int height) {
		BufferedImage img0 = original.getSubimage(x*DEFAULT_CHUNK_SIZE, y*DEFAULT_CHUNK_SIZE, width, height);
		BufferedImage img1 = target.getSubimage(x*DEFAULT_CHUNK_SIZE, y*DEFAULT_CHUNK_SIZE, width, height);
		if(!Images.isSameImage(img0, img1))
			result.add(new ImageDelta(img1,x,y));
	}
}

class SplitCounter{
	int x;
	int y;
	int xRemainder;
	int yRemainder;
	
	SplitCounter(BufferedImage image){
		int chunkSize = ImageCodecImpl.DEFAULT_CHUNK_SIZE;
		x = image.getWidth()/chunkSize;
		y = image.getHeight()/chunkSize;		
		xRemainder = image.getWidth() % chunkSize;
		yRemainder = image.getHeight() % chunkSize;
	}
}