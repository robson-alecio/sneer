package wheel.lang;

import java.util.Arrays;

public class ImmutableByteArray {

	private byte[] _payload;

	public ImmutableByteArray(byte[] bufferToCopy, int bytesToCopy) {
		_payload = Arrays.copyOf(bufferToCopy, bytesToCopy);
		_payload.toString();
	}

}
