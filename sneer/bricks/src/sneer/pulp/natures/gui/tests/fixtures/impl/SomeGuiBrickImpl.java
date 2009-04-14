package sneer.pulp.natures.gui.tests.fixtures.impl;

import sneer.commons.environments.Environment;
import sneer.pulp.natures.gui.tests.fixtures.SomeGuiBrick;
import static sneer.commons.environments.Environments.my;

class SomeGuiBrickImpl implements SomeGuiBrick {

	@Override
	public Thread currentThread() {
		return Thread.currentThread();
	}

	@Override
	public Environment currentEnvironment() {
		return my(Environment.class);
	}

	@Override
	public void run(Runnable runnable) {
		runnable.run();
	}
	
}