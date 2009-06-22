package sneer.bricks.software.code.compilers.java.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import sneer.bricks.software.code.jar.JarBuilder;
import sneer.bricks.software.code.jar.Jars;

public class JarUtils {

	public static File jarGiven(Class<?> clazz) {
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");
		String fullPath = url.getPath();
		String jarPath = fullPath.substring(0, fullPath.indexOf("!"));
		String result;
		try {
			result = new URL(jarPath).getPath();
		} catch (MalformedURLException e) {
			throw new IllegalStateException();
		}
		return new File(result);
	}


	public static void createJar(File file, Class<?> klass) throws IOException, URISyntaxException {
		final JarBuilder builder = my(Jars.class).builder(file);
		try {
			final String fileName = klass.getCanonicalName().replace('.', '/') + ".class";
			final URL url = klass.getResource("/" + fileName);
			final File classFile = new File(url.toURI());
			builder.add(fileName, classFile);
		} finally {
			builder.close();
		}
	}

	public static File fileFor(Class<?> clazz) {
		final String fileName = clazz.getCanonicalName().replace('.', '/') + ".class";
		final URL url = clazz.getResource("/" + fileName);
		return new File(toURI(url));
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
