package sneer.kernel.container.tests.impl;

import sneer.kernel.container.tests.SomeVanillaBrick;

class SomeVanillaBrickImpl implements SomeVanillaBrick {

	@Override
	public Thread brickThread() {
		return Thread.currentThread();
	}

}