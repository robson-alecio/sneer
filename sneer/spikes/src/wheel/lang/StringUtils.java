package wheel.lang;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class StringUtils {
	
	public static byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}
	
	public static String[] splitRight(String line, char separator, int maxParts) {
		final List<String> parts = new ArrayList<String>(maxParts);
		int endIndex = line.length();
		for (int i=maxParts - 1; i>0; --i) {
			final int index = line.lastIndexOf(separator, endIndex - 1);
			if (index < 0) break;
			parts.add(line.substring(index + 1, endIndex));
			endIndex = index;
		}
		parts.add(line.substring(0, endIndex));
		return reversedArrayGiven(parts);
	}

	private static String[] reversedArrayGiven(final List<String> parts) {
		String[] array = parts.toArray(new String[parts.size()]);
		ArrayUtils.reverse(array);
		return array;
	}
	
}
