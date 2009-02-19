package sneer.kernel.container;

import java.util.Arrays;

import sneer.kernel.container.PublicKey;
import sneer.pulp.crypto.Sneer1024;

public class PublicKey {

	private static final long serialVersionUID = 1L;

	private Sneer1024 _sneer1024;
	
	public PublicKey(Sneer1024 sneer1024) {
		_sneer1024 = sneer1024;
	}

	public byte[] bytes() {
		return _sneer1024.bytes();
	}

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
