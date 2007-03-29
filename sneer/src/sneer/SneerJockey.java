package sneer;

import static sneer.kernel.SneerDirectories.latestInstalledSneerJar;
import static sneer.kernel.SneerDirectories.sneerDirectory;

import java.io.File;

import wheel.jars.Jars;


/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
public class SneerJockey {

	public SneerJockey() throws Exception {
		new InstallationWizard(sneerDirectory());
		
		while (true) play(latestSneerJar());
	}

	private void play(File SneerJar) throws Exception {
		System.out.println(SneerJar);
		Jars.runAllowingForClassGC(SneerJar, "sneer.Sneer");
	}

	private File latestSneerJar() {
		File installedJar = latestInstalledSneerJar();
		if (installedJar != null) return installedJar;
		
		return currentJar();
	}

	private File currentJar() {
		return Jars.jarGiven(SneerJockey.class);
	}


}
