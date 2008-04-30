package sneer.bricks.crypto.impl;

import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sneer.bricks.crypto.Crypto;

public class CryptoImpl implements Crypto {

	static {
		//Optimize: remove this static dependency. Use Bouncycastle classes directly
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Override
	public byte[] sneer1024(byte[] input) {
		
		byte[] sha512 = sha512().digest(input);
		byte[] whirlPool = whirlPool().digest(input);

		byte[] result = new byte[sha512.length + whirlPool.length];
		System.arraycopy(sha512, 0, result, 0, sha512.length);
		System.arraycopy(whirlPool, 0, result, sha512.length, whirlPool.length);

		return result;
	}


	private MessageDigest whirlPool() {
		return digester("WHIRLPOOL", "BC");
	}

	private MessageDigest sha512() {
		return digester("SHA-512", "SUN");
	}

	private MessageDigest digester(String algorithm, String provider) {
		try {
			return MessageDigest.getInstance(algorithm, provider);
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

}
