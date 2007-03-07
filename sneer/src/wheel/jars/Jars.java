package wheel.jars;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

import sneerboot.PlatformJockey;


public class Jars {

	public static void runAllowingForClassGC(File jar, String classToInstantiate) throws Exception {
		URLClassLoader loader = createGarbageCollectableClassLoader(jar);
		Thread.currentThread().setContextClassLoader(loader);
		
		loader.loadClass(classToInstantiate).newInstance();
	}
	
	
	private static URLClassLoader createGarbageCollectableClassLoader(File jar) throws MalformedURLException {
		return new URLClassLoader(new URL[]{new URL(jar.getPath())}, bootstrapClassLoader());
	}
	
	
	private static ClassLoader bootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


	public static File jarGiven(Class<?> clazz) {
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");
		String fullPath = url.getPath();
		return new File(fullPath.substring(0, fullPath.indexOf("!")));
	}

}
