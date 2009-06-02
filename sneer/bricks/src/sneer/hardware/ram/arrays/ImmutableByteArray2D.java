package sneer.hardware.ram.arrays;

public interface ImmutableByteArray2D {
	
	byte[] get(int index);

	byte[][] copy();
	
}
