package spikes.klaus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

public class SneerJockey {

	public static void main(String[] argsIgnored) throws Exception {
		new SneerJockey();
	}
	
	public SneerJockey() throws Exception {
		while (true) runSneerSession();
	}

	private static void runSneerSession() throws Exception {
		ClassLoader loader = createGarbageCollectableClassLoader(new File(sneerHome(), "code/bin"));
		loader.loadClass("sneer.main.SneerSession").newInstance();

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
		String override = System.getProperty("sneer.home");
		if (override != null) return new File(override);

		return new File(System.getProperty("user.home"), "sneer");
	}

}