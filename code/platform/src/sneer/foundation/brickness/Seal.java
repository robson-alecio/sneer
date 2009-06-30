package sneer.foundation.brickness;

import java.util.Arrays;

public class Seal {

	private static final long serialVersionUID = 1L;

	private byte[] _bytes;
	
	public Seal(byte[] bytes) {
		_bytes = bytes;
	}

	public byte[] bytes() {
		return _bytes;
	}

	@Override
	public String toString() {
		return "" + _bytes[0] + "," + _bytes[1] + "," +_bytes[2]; 
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(_bytes);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null) return false;
		if (!(other instanceof Seal)) return false;
		return Arrays.equals(_bytes, ((Seal)other)._bytes);
	}
}
