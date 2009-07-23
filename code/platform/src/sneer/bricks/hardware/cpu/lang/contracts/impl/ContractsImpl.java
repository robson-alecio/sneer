package sneer.bricks.hardware.cpu.lang.contracts.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.lang.contracts.Disposable;
import sneer.bricks.hardware.cpu.lang.contracts.Contracts;

class ContractsImpl implements Contracts {

	@Override
	public Contract newContractFor(Disposable service, Object annex) {
		return new ContractImpl(service, annex);
	}

}
