package wheel.lang;

import java.io.UnsupportedEncodingException;

public class StringUtils {
	
	public static byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}
	
}
