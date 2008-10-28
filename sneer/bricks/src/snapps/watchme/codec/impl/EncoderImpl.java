/**
 * 
 */
package snapps.watchme.codec.impl;

import static snapps.watchme.codec.impl.CodecConstants.CELL_SIZE;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Encoder;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import wheel.io.ui.graphics.Images;
import wheel.lang.Pair;
import wheel.lang.exceptions.Hiccup;
import wheel.testutil.Profiler;

class EncoderImpl implements Encoder {
	
	private static Profiler _generateDeltasProfiler = new Profiler("EncoderImpl.generateDeltas()");
	
	@Inject
	private static ImageFactory _imageFactory;	
	
	private Map<Pair<Integer, Integer>, int[]> _lastPixels = new HashMap<Pair<Integer, Integer>, int[]>();
	
	public List<ImageDelta> generateDeltas(BufferedImage shot) throws Hiccup {
		_generateDeltasProfiler.enter();
		final List<ImageDelta> result = new ArrayList<ImageDelta>();
		for (int y = 0; y < shot.getHeight(); y = y + CELL_SIZE) { 
			for (int x = 0; x < shot.getWidth(); x = x + CELL_SIZE) { 
				ImageDelta imageDelta = addImageDeltaIfNecessary(shot, x, y);
				if (imageDelta != null)
					result.add(imageDelta);
			}
		}
		_generateDeltasProfiler.exit();
		return result;
	}	

	private ImageDelta addImageDeltaIfNecessary(BufferedImage _shot, int x, int y) throws Hiccup {
		int width = Math.min(CELL_SIZE, _shot.getWidth() - x);
		int height = Math.min(CELL_SIZE, _shot.getHeight() - y);		
		
		BufferedImage img1 = _shot.getSubimage(x, y, width, height);
		
		int[] currentPixels = Images.pixels(img1);
		int[] previousPixels = _lastPixels.get(Pair.pair(x, y));		
		
		if(previousPixels != null && Arrays.equals(previousPixels, currentPixels)) return null;
		
		_lastPixels.put(Pair.pair(x, y), currentPixels);
		
		byte[] data = _imageFactory.toPngData(img1);
		return new ImageDelta(data, x, y, width, height);	
	}
	
}
