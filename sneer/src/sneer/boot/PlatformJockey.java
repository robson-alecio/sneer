package sneer.boot;

import java.io.File;
import java.net.URL;

import wheel.jars.Jars;

import static sneer.platform.SneerDirectories.latestInstalledPlatformJar; 


/** This guy "plays" (runs) the latest version of the Platform, one after the other. */
public class PlatformJockey {

	public PlatformJockey() throws Exception {
		while (true) play(latestPlatformJar());
	}

	private void play(File platformJar) throws Exception {
		Jars.runAllowingForClassGC(platformJar, "sneer.platform.Platform");
	}

	private File latestPlatformJar() {
		File installed = latestInstalledPlatformJar();
		if (installed != null) return installed;
		
		return sneerJar();
	}

	private File sneerJar() {
		return Jars.jarGiven(PlatformJockey.class);
	}


}
