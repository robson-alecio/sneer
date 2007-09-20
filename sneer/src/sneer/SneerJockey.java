package sneer;

import static sneer.SneerDirectories.latestInstalledSneerJar;
import static sneer.SneerDirectories.sneerDirectory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import wheel.io.Jars;
import wheel.io.ModifiedURLClassLoader;


/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
public class SneerJockey {

	public SneerJockey() throws Exception {
		new InstallationWizard(sneerDirectory());
		
		while (true) play(latestSneerJar());
	}

	private void play(URL sneerJar) throws Exception {
		//FixUrgent: sneerJar variable is not being used inside classloader. If used,
		// scribble app stops working because Brushpacket is not recognized. (class conflict)
		// Everything works fine without it because when you execute the jar,
		// its contents are automatically in the classpath, and when executed using eclipse,
		// the code is in the classpath too.
		// ModifiedURLClassLoader mainLoader = new ModifiedURLClassLoader(new URL[]{sneerJar},this.getClass().getClassLoader()); 
		ModifiedURLClassLoader mainLoader = new ModifiedURLClassLoader(new URL[]{},this.getClass().getClassLoader()); 
		Thread.currentThread().setContextClassLoader(mainLoader); //Might be used by RMI in the future. You never know.
		mainLoader.loadClass("sneer.Sneer").newInstance();
	}

	private URL latestSneerJar() {
		File installedJar = latestInstalledSneerJar();
		if (installedJar == null) return currentJar();

		try {
			return new URL(installedJar.getPath());
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	private URL currentJar() {
		return Jars.jarGiven(SneerJockey.class);
	}


}
