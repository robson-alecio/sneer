/**
 * 
 */
package snapps.watchme.codec.impl;

import static snapps.watchme.codec.impl.CodecConstants.CELL_SIZE;
import static sneer.commons.environments.Environments.my;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Encoder;
import sneer.commons.lang.Pair;
import sneer.hardware.gui.images.Images;
import sneer.skin.image.ImageFactory;
import wheel.lang.ImmutableByteArray;
import wheel.lang.exceptions.Hiccup;
import wheel.testutil.Profiler;

class EncoderImpl implements Encoder {
	
	private static Profiler _generateDeltasProfiler = new Profiler("EncoderImpl.generateDeltas()");
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);	
	
	private Map<Pair<Integer, Integer>, int[]> _previousPixelsByCellCoordinate = new HashMap<Pair<Integer, Integer>, int[]>();
	
	public List<ImageDelta> generateDeltas(BufferedImage shot) throws Hiccup {
		try {
			_generateDeltasProfiler.enter();
			return doGenerateDeltas(shot);
		} finally {
			_generateDeltasProfiler.exit();
		}
	}

	private List<ImageDelta> doGenerateDeltas(BufferedImage shot) throws Hiccup {
		final List<ImageDelta> result = new ArrayList<ImageDelta>();
		
		for (int y = 0; y < shot.getHeight(); y = y + CELL_SIZE) 
			for (int x = 0; x < shot.getWidth(); x = x + CELL_SIZE) 
				addImageDeltaIfNecessary(shot, x, y, result);

		return result;
	}	

	private void addImageDeltaIfNecessary(BufferedImage _shot, int x, int y, List<ImageDelta> result) throws Hiccup {
		int cellWidth = Math.min(CELL_SIZE, _shot.getWidth() - x);
		int cellHeight = Math.min(CELL_SIZE, _shot.getHeight() - y);		
		
		BufferedImage img1 = _shot.getSubimage(x, y, cellWidth, cellHeight);
		
		int[] currentPixels = my(Images.class).pixels(img1);
		int[] previousPixels = _previousPixelsByCellCoordinate.get(Pair.pair(x, y));		
		
		if(previousPixels != null && Arrays.equals(previousPixels, currentPixels))
			return;
		
		_previousPixelsByCellCoordinate.put(Pair.pair(x, y), currentPixels);
		
		byte[] data = _imageFactory.toPngData(img1);
		result.add(new ImageDelta(new ImmutableByteArray(data), x, y));
	}
	
}
