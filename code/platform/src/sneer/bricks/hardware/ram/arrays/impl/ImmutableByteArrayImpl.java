package sneer.bricks.hardware.ram.arrays.impl;

import java.util.Arrays;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;

class ImmutableByteArrayImpl implements ImmutableByteArray {
	
	private final byte[] _payload;
	
	private transient int _hashCode = -1;

	ImmutableByteArrayImpl(byte[] bufferToCopy) {
		this(bufferToCopy, bufferToCopy.length);
	}

	ImmutableByteArrayImpl(byte[] bufferToCopy, int bytesToCopy) {
		_payload = Arrays.copyOf(bufferToCopy, bytesToCopy);
	}
	
	@Override
	public byte get(int index) {
		return _payload[index];
	}

	@Override
	public int copyTo(byte[] dest) {
		int result = _payload.length;
		System.arraycopy(_payload, 0, dest, 0, result);
		return result;
	}

	@Override
	public byte[] copy() {
		return Arrays.copyOf(_payload, _payload.length);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(_payload);
	}

	@Override
	public int hashCode() {
		if (_hashCode == -1)
			_hashCode = Arrays.hashCode(_payload); //Yes one in every 4 billion hash codes will be calculated every time (the case when the hash code actually IS -1 :).

		return _hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImmutableByteArrayImpl other = (ImmutableByteArrayImpl) obj;

		return (Arrays.equals(_payload, other._payload));
	}

}
