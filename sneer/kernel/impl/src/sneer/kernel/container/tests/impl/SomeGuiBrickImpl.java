package sneer.kernel.container.tests.impl;

import sneer.kernel.container.tests.SomeGuiBrick;
import wheel.lang.Threads;

public class SomeGuiBrickImpl implements SomeGuiBrick {

	@Override
	public Thread guiBrickThread() {
		return Thread.currentThread();
	}

	@Override
	public void slowMethod() {
		Threads.sleepWithoutInterruptions(1000);
	}

}
