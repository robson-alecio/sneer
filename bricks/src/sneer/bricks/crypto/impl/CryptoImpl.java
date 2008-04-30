package sneer.bricks.crypto.impl;

import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sneer.bricks.crypto.Crypto;

public class CryptoImpl implements Crypto {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Override
	public byte[] sneer1024(byte[] input) {
		
		byte[] sha512 = sha512().digest(input);
		byte[] whirlPool = whirlPool().digest(input);

		byte[] result = new byte[sha512.length + whirlPool.length];

		for (int i = 0; i < sha512.length; i++) {
			result[i] = sha512[i];
		}
		int offset = sha512.length - 1; 
		for (int i = 0; i < whirlPool.length; i++) {
			result[ offset+ i] = whirlPool[i];
		}
		
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
