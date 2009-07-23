package sneer.bricks.hardware.cpu.lang.contracts.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.lang.contracts.Disposable;

class ContractImpl implements Contract {

	private Disposable _service;
	@SuppressWarnings("unused")	private final Object _annexRefToAvoidGc;

	ContractImpl(Disposable service, Object annex) {
		_service = service;
		_annexRefToAvoidGc = annex;
	}

	@Override
	public void dispose() {
		if (_service == null) return;
		_service.dispose();
		_service = null;
	}

	@Override
	protected void finalize() {
		dispose();
	}

}
