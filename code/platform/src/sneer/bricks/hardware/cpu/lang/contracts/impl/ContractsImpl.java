package sneer.bricks.hardware.cpu.lang.contracts.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Contracts;
import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;

class ContractsImpl implements Contracts {

	@Override
	public WeakContract weakContractFor(Disposable service, Object annexToAvoidGc) {
		return new WeakContractImpl(service, annexToAvoidGc);
	}

}
