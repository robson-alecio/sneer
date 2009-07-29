package sneer.bricks.hardware.cpu.lang.contracts.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;

class WeakContractImpl implements WeakContract {

	@SuppressWarnings("unused")	private final Object _annexToAvoidGc;
	private Disposable _service;

	WeakContractImpl(Disposable service, Object annexToAvoidGc) {
		_annexToAvoidGc = annexToAvoidGc;
		_service = service;
	}

	@Override
	synchronized
	public void dispose() {
		if (_service == null) return;
		_service.dispose();
		_service = null;
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
	}

}
