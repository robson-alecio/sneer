package sneer.bricks.crypto.impl;

import sneer.bricks.crypto.Sneer1024;
import wheel.lang.StringUtils;

public class Sneer1024Impl implements Sneer1024 {

	private byte[] _bytes;
	
	public Sneer1024Impl(byte[] bytes) {
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
}
