package wheel.lang;

import java.util.Arrays;

public class ImmutableByteArray {
	
	private final byte[] _payload;
	
	private transient boolean _isHashCodeCalculated;
	private transient int _hashCode;

	public ImmutableByteArray(byte[] bufferToCopy) {
		this(bufferToCopy, bufferToCopy.length);
	}

	public ImmutableByteArray(byte[] bufferToCopy, int bytesToCopy) {
		_payload = Arrays.copyOf(bufferToCopy, bytesToCopy);
	}

	public int copyTo(byte[] dest) {
		int result = _payload.length;
		System.arraycopy(_payload, 0, dest, 0, result);
		return result;
	}
	
	public byte[] copy() {
		final byte[] copy = new byte[_payload.length];
		System.arraycopy(_payload, 0, copy, 0, copy.length);
		return copy;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(_payload);
	}

	public byte get(int index) {
		return _payload[index];
	}

	@Override
	public int hashCode() {
		if (!_isHashCodeCalculated) {
			_isHashCodeCalculated = true;
			_hashCode = Arrays.hashCode(_payload);
		}
		return _hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImmutableByteArray other = (ImmutableByteArray) obj;

		return (Arrays.equals(_payload, other._payload));
	}

	
}
