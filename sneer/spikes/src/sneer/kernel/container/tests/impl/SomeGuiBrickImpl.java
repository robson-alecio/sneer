package sneer.kernel.container.tests.impl;

import sneer.commons.environments.Environment;
import sneer.kernel.container.tests.SomeGuiBrick;
import sneer.pulp.threads.Threads;
import static sneer.commons.environments.Environments.my;

class SomeGuiBrickImpl implements SomeGuiBrick {

	@Override
	public Thread currentThread() {
		return Thread.currentThread();
	}

	@Override
	public void slowMethod() {
		my(Threads.class).sleepWithoutInterruptions(4000);
	}
	
	@Override
	public Environment currentEnvironment() {
		return my(Environment.class);
	}
}
