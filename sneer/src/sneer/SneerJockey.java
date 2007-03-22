package sneer;

import java.io.File;
import java.net.URL;

import sneer.Sneer;

import wheel.jars.Jars;

import static sneer.kernel.SneerDirectories.latestInstalledSneerJar;


/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
public class SneerJockey {

	public SneerJockey() throws Exception {
		File previousSneerJar = null;
		while (true) {
			File latestSneerJar = latestSneerJar();
			if (latestSneerJar.equals(previousSneerJar)) break;
			previousSneerJar = latestSneerJar;
			
			play(latestSneerJar);
		}
	}

	private void play(File SneerJar) throws Exception {
		System.out.println(SneerJar);
		Jars.runAllowingForClassGC(SneerJar, "sneer.Sneer");
	}

	private File latestSneerJar() {
		File installed = latestInstalledSneerJar();
		if (installed != null) return installed;
		
		return myOwnJar();
	}

	private File myOwnJar() {
		return Jars.jarGiven(SneerJockey.class);
	}


}
