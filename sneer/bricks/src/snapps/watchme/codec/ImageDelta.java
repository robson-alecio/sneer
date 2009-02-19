package snapps.watchme.codec;

import sneer.kernel.container.Tuple;
import wheel.lang.ImmutableByteArray;

public class ImageDelta extends Tuple {

	public final int x;
	public final int y;
	
	public ImmutableByteArray  imageData;
	
	public ImageDelta(ImmutableByteArray imageData_, int x_, int y_) { 
		x = x_;
		y = y_;
		imageData =  imageData_;
	}
}