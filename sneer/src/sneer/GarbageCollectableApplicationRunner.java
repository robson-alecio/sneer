package sneer;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;


public class GarbageCollectableApplicationRunner {
	
	public static void run(String appClassName) throws Exception {
		start(sneerJarPath(), appClassName);
	}
	
	
	private static void start(String jarPath, String appClassName) throws Exception {
		URLClassLoader loader = createGarbageCollectableClassLoader(jarPath);
		Thread.currentThread().setContextClassLoader(loader);
		
		loader.loadClass(appClassName).newInstance();
	}

	
	private static URLClassLoader createGarbageCollectableClassLoader(String path) throws MalformedURLException {
		return new URLClassLoader(new URL[]{new URL(path)}, vmBootstrapClassLoader());
	}
	
	
	private static ClassLoader vmBootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


	private static String sneerJarPath() {
		URL url = GarbageCollectableApplicationRunner.class.getResource(GarbageCollectableApplicationRunner.class.getSimpleName() + ".class");
		String fullPath = url.getPath();
		return fullPath.substring(0, fullPath.indexOf("!"));
	}

}
