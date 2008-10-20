package wheel.lang;

import java.util.Arrays;

public class ImmutableByteArray {

	private byte[] _payload;

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
	
	

}
