package sneer.bricks.hardware.ram.arrays;

public interface ImmutableByteArray {
	
	byte get(int index);

	byte[] copy();
	/** Returns the number of bytes copied to dest (length of this array)*/
	int copyTo(byte[] dest);
	
}
