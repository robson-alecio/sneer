package sneer.main;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
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
		
		my(SnappStarter.class).startSnapps();
		
		my(Threads.class).waitUntilCrash();
	}

	private static Environment container() {
		return Brickness.newBrickContainer();
	}
	
	private static void configure(DirectoryConfig config) {
		config.ownBinDirectory().set(SneerDirectories.OWN_BIN);
		config.platformBinDirectory().set(SneerDirectories.PLATFORM_BIN);
	}

}
