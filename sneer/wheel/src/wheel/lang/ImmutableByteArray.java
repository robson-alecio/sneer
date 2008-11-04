package wheel.lang;

import java.util.Arrays;

public class ImmutableByteArray {
	
	public static ImmutableByteArray takeOwnership(byte[] payload) {
		return new ImmutableByteArray(payload);
	}

	private byte[] _payload;

	private ImmutableByteArray(byte[] privatePayload) {
		_payload = privatePayload;
	}
	
	public ImmutableByteArray(byte[] bufferToCopy, int bytesToCopy) {
		this(Arrays.copyOf(bufferToCopy, bytesToCopy));
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
		return Arrays.hashCode(_payload);
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
