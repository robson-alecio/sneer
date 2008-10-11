package snapps.watchme.codec;

import sneer.pulp.tuples.Tuple;

public class ImageDelta extends Tuple {

	public final int x;
	public final int y;
	
	public final int width;
	public final int height;
	
	public int[]  imageData;
	
	public ImageDelta(int[] imageData_, int x_, int y_, int width_, int height_) { 
		x = x_;
		y = y_;
		width = width_;
		height = height_;
		imageData =  imageData_;
	}
}