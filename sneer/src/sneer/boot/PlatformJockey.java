package sneer.boot;

import java.io.File;
import java.net.URL;

import sneer.kernel.Platform;

import wheel.jars.Jars;

import static sneer.kernel.SneerDirectories.latestInstalledPlatformJar;


/** This guy "plays" (runs) the latest version of the Platform, one after the other. */
public class PlatformJockey {

	public PlatformJockey() throws Exception {
		File previousPlatformJar = null;
		while (true) {
			File latestPlatformJar = latestPlatformJar();
			if (latestPlatformJar.equals(previousPlatformJar)) break;
			previousPlatformJar = latestPlatformJar;
			
			play(latestPlatformJar);
		}
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
