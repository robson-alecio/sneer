package sneer.pulp.lang.impl;

import static sneer.commons.environments.Environments.my;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sneer.hardware.cpu.lang.Lang;
import sneer.pulp.lang.StringUtils;

class StringUtilsImpl implements StringUtils {

	private static String[] reversedArrayGiven(final List<String> parts) {
		String[] array = parts.toArray(new String[parts.size()]);
		my(Lang.class).arrays().reverse(array);

		return array;
	}

	@Override
	public byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");

		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see sneer.pulp.lang.StringUtils#splitRight(java.lang.String, char, int)
	 */
	@Override
	public String[] splitRight(String line, char separator, int maxParts) {
		final List<String> parts = new ArrayList<String>(maxParts);
		int endIndex = line.length();

		for (int i = maxParts - 1; i > 0; --i) {
			final int index = line.lastIndexOf(separator, endIndex - 1);
			if (index < 0) break;
			parts.add(line.substring(index + 1, endIndex));
			endIndex = index;
		}

		parts.add(line.substring(0, endIndex));
		return reversedArrayGiven(parts);
	}	
}
