package sneer.main;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class SneerSession implements Runnable {

	public SneerSession() {
		Environments.runWith(container(), this);
	}

	public void run() {
		my(SnappStarter.class).startSnapps();
		my(Threads.class).waitUntilCrash();
	}

	private Environment container() {
		return Brickness.newBrickContainer(new SneerStoragePath());
	}
}