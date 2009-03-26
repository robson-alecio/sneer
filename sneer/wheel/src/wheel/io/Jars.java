package wheel.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;



public class Jars {

	public static void runAllowingForClassGC(URL jar, String classToInstantiate) throws Exception {
		URLClassLoader loader = createGarbageCollectableClassLoader(jar);
		loader.loadClass(classToInstantiate).newInstance();
	}
	
	
	public static URLClassLoader createGarbageCollectableClassLoader(URL jar) throws Exception {
		return new URLClassLoader(new URL[]{jar}, bootstrapClassLoader());
	}
	
	
	public static ClassLoader bootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


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
		final JarBuilder builder = new JarBuilder(file);
		try {
			final String fileName = klass.getCanonicalName().replace('.', '/') + ".class";
			final URL url = klass.getResource("/" + fileName);
			final File classFile = new File(url.toURI());
			builder.add(fileName, classFile);
		} finally {
			builder.close();
		}
	}
	

}
