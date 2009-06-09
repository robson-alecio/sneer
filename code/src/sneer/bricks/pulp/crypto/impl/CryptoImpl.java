package sneer.bricks.pulp.crypto.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;

class CryptoImpl implements Crypto {

	static {
		Security.addProvider(new BouncyCastleProvider()); //Optimize: remove this static dependency. Use Bouncycastle classes directly
	}

	private final DigesterImpl _digester = new DigesterImpl(messageDigest("SHA-512", "SUN"), messageDigest("WHIRLPOOL", "BC"));
	
	@Override
	public synchronized Sneer1024 digest(byte[] input) {
		byte[] sha512 = _digester.sha512().digest(input);
		byte[] whirlPool = _digester.whirlPool().digest(input);
		byte[] result = _digester.merge(sha512, whirlPool); 
		return wrap(result);
	}

	private Sneer1024 wrap(byte[] sneer1024Bytes) {
		return new Sneer1024Impl(sneer1024Bytes);
	}

	@Override
	public Digester digester() {
		return new DigesterImpl(messageDigest("SHA-512", "SUN"), messageDigest("WHIRLPOOL", "BC"));
	}

	private MessageDigest messageDigest(String algorithm, String provider) {
		try {
			return MessageDigest.getInstance(algorithm, provider);
		} catch (Exception e) {
			throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

	@Override
	public Sneer1024 digest(File file) throws IOException {
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			return digest(my(IO.class).streams().toByteArray(input));
		} finally {
			try { input.close(); } catch (Throwable ignore) { }
		}
	}

	@Override
	public Sneer1024 unmarshallSneer1024(byte[] bytes) {
		return new Sneer1024Impl(bytes);
	}

}

class DigesterImpl implements Digester {
	
	private MessageDigest _sha512;
	
	private MessageDigest _whirlPool;
	
	DigesterImpl(MessageDigest sha512, MessageDigest whirlPool) {
		_sha512 = sha512;
		_whirlPool = whirlPool;
	}
	
	@Override
	public byte[] digest() {
		byte[] sha512 = _sha512.digest();
		byte[] whirlPool = _whirlPool.digest();
		return merge(sha512, whirlPool);
	}
	
	@Override
	public void update(InputStream is) throws IOException {
		byte[] bytes = my(IO.class).streams().toByteArray(is);
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
