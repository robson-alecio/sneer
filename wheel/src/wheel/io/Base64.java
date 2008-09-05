package wheel.io;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class Base64 {

	public static String encode(final byte[] bytes) {
		return new Base64Encoder().encode(bytes);
	}

	public static String encode(String text) {
		return encode(text.getBytes());
	}

}
