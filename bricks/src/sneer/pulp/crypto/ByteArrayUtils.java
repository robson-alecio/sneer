package sneer.pulp.crypto;

import org.bouncycastle.util.encoders.Hex;

public class ByteArrayUtils {

	public static String toHexa(byte[] bytes) {
		return new String(Hex.encode(bytes));
	}

}
