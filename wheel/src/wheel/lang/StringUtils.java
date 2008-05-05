package wheel.lang;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.util.encoders.Hex;

public class StringUtils {
	
	public static byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}
	
	public static String toHexa(byte[] bytes) {
		return new String(Hex.encode(bytes));
	}
	
}
