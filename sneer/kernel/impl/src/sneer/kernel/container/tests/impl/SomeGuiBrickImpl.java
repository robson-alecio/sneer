package sneer.kernel.container.tests.impl;

import sneer.kernel.container.tests.SomeGuiBrick;

public class SomeGuiBrickImpl implements SomeGuiBrick {

	@Override
	public Thread guiBrickThread() {
		return Thread.currentThread();
	}

	@Override
	public void slowMethod() throws InterruptedException {
		Thread.sleep(10000);
	}

}
