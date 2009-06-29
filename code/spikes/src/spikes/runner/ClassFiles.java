package spikes.runner;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

class ClassFiles {
	
	static File classpathRootFor(Class<?> brick) {
		File result = fileFor(brick);
		int depth = brick.getName().split("\\.").length;
		while (depth-- != 0)
			result = result.getParentFile();
		return result;
	}

	private static File fileFor(Class<?> brick) {
		final String fileName = brick.getCanonicalName().replace('.', '/') + ".class";
		final URL url = brick.getResource("/" + fileName);
		return new File(ClassFiles.toURI(url));
	}
	
	private static URI toURI(final URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalStateException();
		}
	}

}
