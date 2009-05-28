package sneer.installer;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
class SneerJockey {

	@SuppressWarnings("unused")
	private final File _sneerHome;

	SneerJockey(File sneerHome) throws Exception {
		_sneerHome = sneerHome;
		
//		while (true)
			play();
	}

	private void play() throws Exception {
		//TODO: GCollectable class loader:  new File(_sneerHome, "bin");
		//loadClass "main.Sneer".newInstance();
	}

	public static URLClassLoader createGarbageCollectableClassLoader(URL jar) throws Exception {
		return new URLClassLoader(new URL[]{jar}, bootstrapClassLoader());
	}
	
	
	private static ClassLoader bootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


	
}