package sneer.bricks.pulp.crypto.impl;

import java.security.MessageDigest;

import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;

class DigesterImpl implements Digester {
	
	private MessageDigest _sha512;
	private MessageDigest _whirlPool;

	
	DigesterImpl(MessageDigest sha512, MessageDigest whirlPool) {
		_sha512 = sha512;
		_whirlPool = whirlPool;
	}
	
	
	@Override
	public Sneer1024 digest() {
		byte[] sha512 = _sha512.digest();
		byte[] whirlPool = _whirlPool.digest();
		return new Sneer1024Impl(merge(sha512, whirlPool));
	}
	
	@Override
	public void update(byte[] bytes) {
		_sha512.update(bytes);
		_whirlPool.update(bytes);
	}

	MessageDigest whirlPool() {
		return _whirlPool;
	}
	
	MessageDigest sha512() {
		return _sha512;
	}
	
	byte[] merge(byte[] sha512, byte[] whirlPool) {
		byte[] result = new byte[sha512.length + whirlPool.length];
		System.arraycopy(sha512, 0, result, 0, sha512.length);
		System.arraycopy(whirlPool, 0, result, sha512.length, whirlPool.length);
		return result;
	}
	
}