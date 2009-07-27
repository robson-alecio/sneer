package sneer.main;

import static sneer.foundation.environments.Environments.my;
import static sneer.main.SneerDirectories.DATA;
import static sneer.main.SneerDirectories.LOG_FILE;
import static sneer.main.SneerDirectories.OWN_BIN;
import static sneer.main.SneerDirectories.PLATFORM_BIN;

import java.io.File;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.workers.notifier.LogNotifier;
import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class SneerSession implements Runnable {

	public SneerSession() {
		Environments.runWith(container(), this);
	}

	public void run() {
		configure(my(DirectoryConfig.class));
		startLogging();
		my(SnappStarter.class).startSnapps();
		my(Threads.class).waitUntilCrash();
	}

	private void startLogging() {
		my(LogNotifier.class);
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
