package sneer.bricks.hardware.ram.arrays;

import sneer.foundation.brickness.Brick;

@Brick
public interface ImmutableArrays {

	ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy);
	ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy, int bytesToCopy);
	
	ImmutableByteArray2D newImmutableByteArray2D(byte[][] array2D);
	
}
