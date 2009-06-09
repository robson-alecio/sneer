package sneer.bricks.hardware.ram.arrays.impl;

import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray2D;

class ImmutableArraysImpl implements ImmutableArrays {

	@Override
	public ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy) {
		return new ImmutableByteArrayImpl(bufferToCopy);
	}

	@Override
	public ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy, int bytesToCopy) {
		return new ImmutableByteArrayImpl(bufferToCopy, bytesToCopy);
	}

	@Override
	public ImmutableByteArray2D newImmutableByteArray2D(byte[][] bufferToCopy) {
		return new ImmutableByteArray2DImpl(bufferToCopy);
	}

}
