package wheel.jars;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Jars {

	public static void runAllowingForClassGC(URL jar, String classToInstantiate) throws Exception {
		URLClassLoader loader = createGarbageCollectableClassLoader(jar);
		loader.loadClass(classToInstantiate).newInstance();
	}
	
	
	private static URLClassLoader createGarbageCollectableClassLoader(URL jar) throws Exception {
		return new URLClassLoader(getClasspathWithLibs(jar), bootstrapClassLoader());
	}
	
	
	private static ClassLoader bootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


	public static URL jarGiven(Class<?> clazz) {
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");
		String fullPath = url.getPath();
		
		System.out.println(fullPath);
		String path = fullPath.substring(0, fullPath.indexOf("!"));
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e); 
		}
	}
	
	private static URL[] getClasspathWithLibs(URL jar) throws IOException {
		List<URL> result = new LinkedList<URL>();
		result.add(jar);

        // Get the jar file
        JarURLConnection conn = (JarURLConnection)new URL("jar:" + jar + "!/").openConnection();
        JarFile jarFile = conn.getJarFile();
        
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".jar")) {
				String path = "file:" + jar.getPath() + "!/" + entry.getName();
				System.out.println(path);
				result.add(new URL((path)));
			}
		}
		
		return result.toArray(new URL[0]);
	}

}
