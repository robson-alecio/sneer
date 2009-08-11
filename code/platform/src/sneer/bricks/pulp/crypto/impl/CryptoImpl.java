package sneer.bricks.pulp.crypto.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

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
	public Digester newDigester() {
		return new DigesterImpl(messageDigest("SHA-512", "SUN"), messageDigest("WHIRLPOOL", "BC"));
	}

	private MessageDigest messageDigest(String algorithm, String provider) {
		try {
			return MessageDigest.getInstance(algorithm, provider);
		} catch (Exception e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
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

	@Override
	public String toHexa(byte[] bytes) {
		return new String(Hex.encode(bytes));
	}

}


