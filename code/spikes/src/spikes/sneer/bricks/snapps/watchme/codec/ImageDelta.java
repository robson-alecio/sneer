package spikes.sneer.bricks.snapps.watchme.codec;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.brickness.Tuple;

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