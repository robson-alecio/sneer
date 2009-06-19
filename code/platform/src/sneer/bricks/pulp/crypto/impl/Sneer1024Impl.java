package sneer.bricks.pulp.crypto.impl;

import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;

import sneer.bricks.pulp.crypto.Sneer1024;

class Sneer1024Impl implements Sneer1024 {

	private static final long serialVersionUID = 1L;

	private byte[] _bytes;
	
	public Sneer1024Impl(byte[] bytes) {
		if (bytes.length != 128) throw new IllegalArgumentException();		
		_bytes = bytes;
	}

	@Override
	public byte[] bytes() {
		return _bytes;
	}

	@Override
	public String toHexa() {
		return new String(Hex.encode(_bytes));
	}

	@Override
	public String toString() {
		return toHexa().substring(0, 10) + ".."; 
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(_bytes);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null) return false;
		if (!(other instanceof Sneer1024)) return false;
		return Arrays.equals(_bytes, ((Sneer1024)other).bytes());
	}
}
