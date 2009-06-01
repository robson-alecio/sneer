package sneer.hardware.ram.arrays;

import sneer.brickness.Brick;

@Brick
public interface ImmutableArrays {

	ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy);
	ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy, int bytesToCopy);
	
}
