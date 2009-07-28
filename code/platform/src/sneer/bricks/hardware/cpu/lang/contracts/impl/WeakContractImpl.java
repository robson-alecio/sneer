package sneer.bricks.hardware.cpu.lang.contracts.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;

class WeakContractImpl implements WeakContract {

	private Disposable _service;

	WeakContractImpl(Disposable service) {
		_service = service;
	}

	@Override
	synchronized
	public void dispose() {
		_service.dispose();
		_service = null;
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
	}

}
