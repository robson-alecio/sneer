package sneer.main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class Sneer {

	public static void main(String[] argsIgnored) throws Exception {
		new Sneer();
	}
	
	public Sneer() throws Exception {
		independentClassLoader().loadClass("sneer.main.SneerSession").newInstance();
	}

	private URLClassLoader independentClassLoader() {
		ClassLoader noParent = null;
		return new URLClassLoader(classpath(), noParent);
	}

	private URL[] classpath() {
		return new URL[] {
			toURL(SneerDirectories.OWN_BIN),
			toURL(SneerDirectories.PLATFORM_BIN)
		};
	}

	private URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}
	
}