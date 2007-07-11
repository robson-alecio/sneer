package sneer;

import static sneer.SneerDirectories.latestInstalledSneerJar;
import static sneer.SneerDirectories.sneerDirectory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import wheel.io.Jars;


/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
public class SneerJockey {

	public SneerJockey() throws Exception {
		new InstallationWizard(sneerDirectory());
		
		while (true) play(latestSneerJar());
	}

	
	private void play(URL SneerJar) throws Exception {
		Jars.runAllowingForClassGC(SneerJar, "sneer.Sneer");
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
