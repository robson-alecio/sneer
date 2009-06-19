package sneer.foundation.testsupport;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class ClassFiles {

	public static File fileFor(Class<?> clazz) {
		final String fileName = clazz.getCanonicalName().replace('.', '/') + ".class";
		final URL url = clazz.getResource("/" + fileName);
		try {
			return new File(toURI(url));
		} catch (RuntimeException e) {
			throw new IllegalStateException("Class: " + clazz + " url: " + url, e);
		}
	}

	static public File classpathRootFor(Class<?> clazz) {
		File result = fileFor(clazz);
		int depth = clazz.getName().split("\\.").length;
		while (depth-- != 0)
			result = result.getParentFile();
		return result;
	}

	private static URI toURI(final URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalStateException();
		}
	}
	

}
