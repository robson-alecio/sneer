package sneer.bricks.hardware.cpu.threads.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;

class Stepper implements Runnable, Contract {

	private final Steppable _steppable;
	private volatile boolean _isDisposed = false;

	Stepper(Steppable steppable) {
		_steppable = steppable;
	}

	@Override
	public void run() {
		while (!_isDisposed) _steppable.step();
	}

	@Override
	public void dispose() {
		_isDisposed = true;
	}
}
