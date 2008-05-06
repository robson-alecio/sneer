package sneer.bricks.crypto.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.Security;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Digester;

public class CryptoImpl implements Crypto {

	static {
		Security.addProvider(new BouncyCastleProvider()); //Optimize: remove this static dependency. Use Bouncycastle classes directly
	}

	private final DigesterImpl _digester = new DigesterImpl(messageDigest("SHA-512", "SUN"), messageDigest("WHIRLPOOL", "BC"));
	
	@Override
	public synchronized byte[] sneer1024(byte[] input) {
		byte[] sha512 = _digester.sha512().digest(input);
		byte[] whirlPool = _digester.whirlPool().digest(input);
		return _digester.merge(sha512, whirlPool);
	}

	@Override
	public Digester sneer1024() {
		return new DigesterImpl(messageDigest("SHA-512", "SUN"), messageDigest("WHIRLPOOL", "BC"));
	}

	private MessageDigest messageDigest(String algorithm, String provider) {
		try {
			return MessageDigest.getInstance(algorithm, provider);
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
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
		byte[] bytes = read(is);
		_sha512.update(bytes);
		_whirlPool.update(bytes);
		//System.out.println(" " + StringUtils.toHexa(bytes));
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
	
	private byte[] read(InputStream is) throws IOException {
		return IOUtils.toByteArray(is);
	}
	
}
