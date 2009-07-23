package sneer.bricks.hardware.cpu.threads.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.ref.WeakReference;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.Contracts;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.io.log.Logger;

class Stepper implements Runnable, Disposable {

	private final WeakReference<Steppable> _steppable;
	private final String _stepperToString;
	private volatile boolean _isDisposed = false;

	Stepper(Steppable steppable) {
		_steppable = new WeakReference<Steppable>(steppable);
		_stepperToString = steppable.toString(); 
	}

	@Override
	public void run() {
		while (true) {
			if (_isDisposed) break;
			Steppable steppable = _steppable.get();
			if (steppable == null) break;
			
			steppable.step();
		}
		
		my(Logger.class).log("Stepper {} has been disposed.", _stepperToString);
	}

	Contract contract() {
		return my(Contracts.class).newContractFor(this, _steppable.get());
	}

	@Override
	public void dispose() {
		_isDisposed = true;
	}
}
