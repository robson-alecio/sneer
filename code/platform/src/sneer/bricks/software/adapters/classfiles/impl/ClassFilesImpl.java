package sneer.bricks.software.adapters.classfiles.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import sneer.bricks.software.adapters.classfiles.ClassFiles;

class ClassFilesImpl implements ClassFiles {
	
	@Override
	public File classpathRootFor(Class<?> brick) {
		File result = fileFor(brick);
		int depth = brick.getName().split("\\.").length;
		while (depth-- != 0)
			result = result.getParentFile();
		return result;
	}

	private File fileFor(Class<?> brick) {
		final String fileName = brick.getCanonicalName().replace('.', '/') + ".class";
		final URL url = brick.getResource("/" + fileName);
		return new File(toURI(url));
	}
	
	private URI toURI(final URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalStateException();
		}
	}
}