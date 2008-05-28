package sneer.bricks.keymanager.impl;

import java.util.Arrays;

import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.PublicKey;

class PublicKeyImpl implements PublicKey {

	private static final long serialVersionUID = 1L;

	private Sneer1024 _sneer1024;
	
	public PublicKeyImpl(Sneer1024 sneer1024) {
		_sneer1024 = sneer1024;
	}

	@Override
	public byte[] bytes() {
		return _sneer1024.bytes();
	}

	@Override
	public String toHexa() {
		return _sneer1024.toHexa();
	}

	@Override
	public String toString() {
		return _sneer1024.toString(); 
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(bytes());
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null) return false;
		if (!(other instanceof PublicKey)) return false;
		return Arrays.equals(bytes(), ((PublicKey)other).bytes());
	}
}
