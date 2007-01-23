
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;


public class Boot {
	
	private static final String SUCCESSOR_JAR_PROPERTY = "sneer.sucessor.JarPath";
	private static final String SUCCESSOR_MAIN_CLASS_PROPERTY = "sneer.sucessor.MainClass";

	
	public static void main(String[] ignored) {
		try {
			tryToRun();
		} catch (Throwable t) {
			show(t);
		}
	}


	private static void tryToRun() throws Exception {
		checkJavaVersion6OtherwiseExit();
		
		setSuccessor(sneerJarPath(), "sneer.strap.Strap");
		while (true) executeSuccessor();
	}
	
	
	private static void executeSuccessor() throws Exception {
		System.out.println("> > > > > > " + System.getProperty(SUCCESSOR_JAR_PROPERTY));
		String successorJarPath = System.clearProperty(SUCCESSOR_JAR_PROPERTY);
		System.out.println("> > > > > > " + System.getProperty(SUCCESSOR_JAR_PROPERTY));
		System.out.println("> > > > > > " + successorJarPath);
		if (successorJarPath == null) System.exit(0);
		
		URLClassLoader loader = createGarbageCollectableClassLoader(successorJarPath);
		Thread.currentThread().setContextClassLoader(loader);
		
		String mainClassName = System.getProperty(SUCCESSOR_MAIN_CLASS_PROPERTY);
		loader.loadClass(mainClassName).newInstance();
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
		URL url = Boot.class.getResource(Boot.class.getSimpleName() + ".class");
		String fullPath = url.getPath();
		return fullPath.substring(0, fullPath.indexOf("!"));
	}


	private static void checkJavaVersion6OtherwiseExit() {
		String version = System.getProperty("java.specification.version");
		if (Float.parseFloat(version) >= 1.6f) return;
		
		String message = "You are running Sneer on Java version " + version + ".\n\n" +
				" You need Java version 6 or newer.";
		showError(message);
		System.exit(-1);
	}


	private static void show(Throwable t) {
		t.printStackTrace();
		showError(t.toString());
	}

	
	private static void showError(String message) {
		try {
			JOptionPane.showOptionDialog(null, " " + message + "\n\n", "Sneer", JOptionPane.ERROR_MESSAGE, 0, null, new Object[]{"Exit"}, "Exit");
		} catch (RuntimeException headlessExceptionDoesNotExistInOlderJREs) {
			System.out.println("ERROR: " + message);
		}
	}


	private static void setSuccessor(String jarPath, String mainClass) {
		System.setProperty(SUCCESSOR_JAR_PROPERTY, jarPath);
		System.setProperty(SUCCESSOR_MAIN_CLASS_PROPERTY, mainClass);
	}

}
