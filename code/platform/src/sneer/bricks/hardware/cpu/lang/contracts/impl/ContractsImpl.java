package sneer.bricks.hardware.cpu.lang.contracts.impl;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.Contracts;

class ContractsImpl implements Contracts {

	@Override
	public WeakContract newContractFor(Disposable service, Object annex) {
		return new ContractImpl(service, annex);
	}

}
