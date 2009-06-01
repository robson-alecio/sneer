package sneer.hardware.ram.arrays.impl;

import sneer.hardware.ram.arrays.ImmutableArrays;
import sneer.hardware.ram.arrays.ImmutableByteArray;

class ImmutableArraysImpl implements ImmutableArrays {

	@Override
	public ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy) {
		return new ImmutableByteArrayImpl(bufferToCopy);
	}

	@Override
	public ImmutableByteArray newImmutableByteArray(byte[] bufferToCopy, int bytesToCopy) {
		return new ImmutableByteArrayImpl(bufferToCopy, bytesToCopy);
	}

}
