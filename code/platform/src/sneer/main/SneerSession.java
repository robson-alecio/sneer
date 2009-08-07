package sneer.main;

import static sneer.foundation.environments.Environments.my;
import static sneer.main.SneerDirectories.DATA;
import static sneer.main.SneerDirectories.LOG_FILE;
import static sneer.main.SneerDirectories.OWN_BIN;
import static sneer.main.SneerDirectories.PLATFORM_BIN;

import java.io.File;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.exceptions.robust.RobustExceptionLogging;
import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.bricks.snapps.system.log.sysout.LogToSysout;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.bricks.software.folderconfig.FolderConfig;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class SneerSession implements Runnable {

	public SneerSession() {
		Environments.runWith(container(), this);
	}

	public void run() {
		configure(my(FolderConfig.class));

		startLogging();
		my(SnappStarter.class).startSnapps();
		
		my(Threads.class).waitUntilCrash();
	}

	private void startLogging() {
		my(RobustExceptionLogging.class).turnOn();
		my(LogToSysout.class);
	}

	private static Environment container() {
		return Brickness.newBrickContainer();
	}
	
	
	private static void configure(FolderConfig dirs) {
		set(dirs.ownBinFolder(), OWN_BIN);
		set(dirs.platformBinFolder(), PLATFORM_BIN);
		set(dirs.dataFolder(), DATA);
		
		dirs.logFile().set(LOG_FILE);
	}

	private static void set(Immutable<File> property, File folder) {
		if (!folder.exists() && !folder.mkdirs()) throw new IllegalStateException("Unable to create folder " + property);
		property.set(folder);
	}

}
