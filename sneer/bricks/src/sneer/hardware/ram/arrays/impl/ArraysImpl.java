package sneer.hardware.ram.arrays.impl;

import sneer.hardware.ram.arrays.Arrays;
import sneer.hardware.ram.arrays.ImmutableByteArray;

public class ArraysImpl implements Arrays {

	@Override
	public ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy) {
		return new ImmutableByteArrayImpl(bufferToCopy);
	}

	@Override
	public ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy, int bytesToCopy) {
		return new ImmutableByteArrayImpl(bufferToCopy, bytesToCopy);
	}

}
