package sneer.main;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class SneerSession {

	public static void main(String[] ignored) throws Exception {
		Environment environment = Brickness.newBrickContainer(new SneerStoragePath());
		new SneerSession(environment);
	}
	
	public SneerSession(Environment environment) {
		Environments.runWith(environment, new Runnable(){ @Override public void run() {
			runInEnvironment();
		}});
	}

	private void runInEnvironment() {
		my(SnappStarter.class);
		my(Threads.class).waitUntilCrash();
	}

}