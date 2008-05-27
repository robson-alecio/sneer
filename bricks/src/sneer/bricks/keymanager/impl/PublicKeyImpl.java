package sneer.bricks.keymanager.impl;

import java.util.Arrays;

import sneer.bricks.keymanager.PublicKey;
import wheel.lang.StringUtils;

class PublicKeyImpl implements PublicKey {

	private byte[] _bytes;
	
	public PublicKeyImpl(byte[] bytes) {
		if (bytes.length != 128) throw new IllegalArgumentException();		
		_bytes = bytes;
	}

	@Override
	public byte[] bytes() {
		return _bytes;
	}

	@Override
	public String toHexa() {
		return StringUtils.toHexa(_bytes);
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
		if (!(other instanceof PublicKey)) return false;
		return Arrays.equals(_bytes, ((PublicKey)other).bytes());
	}

}
