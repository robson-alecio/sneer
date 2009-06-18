package sneer.bricks.hardware.ram.arrays.impl;

import java.util.Arrays;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray2D;

public class ImmutableByteArray2DImpl implements ImmutableByteArray2D {
	
	private final byte[][] _payload;
	
	public ImmutableByteArray2DImpl(byte[][] bufferToCopy) {
		_payload = copy(bufferToCopy);
	}

	@Override
	public byte[][] copy() {
		return copy(_payload);
	}

	@Override
	public byte[] get(int index) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	static private byte[][] copy(byte[][] original) {
		byte[][] result = new byte[original.length][0];
		
		for (int i = 0; i < original.length; i++)
			result[i] = Arrays.copyOf(original[i], original[i].length);
		
		return result;
	}


}
