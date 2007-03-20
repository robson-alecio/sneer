package sneer.boot;

import java.io.File;
import java.net.URL;

import sneer.kernel.Kernel;

import wheel.jars.Jars;

import static sneer.kernel.SneerDirectories.latestInstalledKernelJar;


/** This guy "plays" (runs) the latest version of the Kernel, one after the other. */
public class KernelJockey {

	public KernelJockey() throws Exception {
		File previousKernelJar = null;
		while (true) {
			File latestKernelJar = latestKernelJar();
			if (latestKernelJar.equals(previousKernelJar)) break;
			previousKernelJar = latestKernelJar;
			
			play(latestKernelJar);
		}
	}

	private void play(File kernelJar) throws Exception {
		System.out.println(kernelJar);
		Jars.runAllowingForClassGC(kernelJar, "sneer.kernel.Kernel");
	}

	private File latestKernelJar() {
		File installed = latestInstalledKernelJar();
		if (installed != null) return installed;
		
		return sneerJar();
	}

	private File sneerJar() {
		return Jars.jarGiven(KernelJockey.class);
	}


}
