package sneer.main;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

import static sneer.main.SneerDirectories.*;

public class SneerSession implements Runnable {

	public SneerSession() {
		Environments.runWith(container(), this);
	}

	public void run() {
		configure(my(DirectoryConfig.class));
		
		my(SnappStarter.class).startSnapps();
		
		my(Threads.class).waitUntilCrash();
	}

	private static Environment container() {
		return Brickness.newBrickContainer();
	}
	
	
	private static void configure(DirectoryConfig dirs) {
		set(dirs.ownBinDirectory(), OWN_BIN);
		set(dirs.platformBinDirectory(), PLATFORM_BIN);
		set(dirs.dataDirectory(), DATA);
		
		dirs.logFile().set(LOG_FILE);
	}

	private static void set(Immutable<File> property, File directory) {
		if (!directory.exists() && !directory.mkdirs()) throw new IllegalStateException("Unable to create directory " + property);
		property.set(directory);
	}

}
