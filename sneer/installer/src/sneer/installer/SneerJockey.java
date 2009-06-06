package sneer.installer;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
class SneerJockey {

	private final File _sneerHome;
	
	SneerJockey(File sneerHome) throws Exception {
		_sneerHome = sneerHome;
		
		System.setSecurityManager(new PermissiveSecurityManager());
		
		while (true) play();
	}

	private void play() throws Exception {
		JOptionPane.showMessageDialog(null, "Playing Sneer...");

		ClassLoader loader = createGarbageCollectableClassLoader(new File(_sneerHome, "bin"));
		loader.loadClass("main.Sneer").newInstance();
		
		JOptionPane.showMessageDialog(null, "Played Sneer...");

	}

	public static URLClassLoader createGarbageCollectableClassLoader(File binDirectory) throws Exception {
		return new URLClassLoader(new URL[]{ binDirectory.toURI().toURL() }, bootstrapClassLoader());
	}

	private static ClassLoader bootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}
}