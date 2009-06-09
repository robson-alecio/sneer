package main;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

public class Sneer {

	public Sneer() throws Exception {
		while (true) runSneerSession();
	}

	private static void runSneerSession() throws Exception {
		ClassLoader loader = createGarbageCollectableClassLoader(new File(sneerHome(), "bin"));
		loader.loadClass("main.SneerSession").newInstance();

		WeakReference<ClassLoader> weakLoader = new WeakReference<ClassLoader>(loader);
		loader = null;
		System.gc();
		if (weakLoader.get() != null) exitVM();
	}

	private static void exitVM() {
		String title = "Exiting VM";
		String message =
			"This Sneer session did not garbage collect completely./n" +
			"/n" +
			"Some Java system resource such as a JFrame might still be/n" +
			"holding a reference to some Sneer object./n" +
			"/n" +
			"Sneer will now exit the VM. You will have to restart Sneer manually.";
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
		
		System.exit(-1);
	}

	private static URLClassLoader createGarbageCollectableClassLoader(File binDirectory) throws Exception {
		ClassLoader noParent = null;
		return new URLClassLoader(new URL[]{ binDirectory.toURI().toURL() }, noParent);
	}

	
	private static File sneerHome() {
		return new File(userHome(), "sneer");
	}
	
	private static String userHome() {
		String override = System.getProperty("home_override");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}

}