package sneer.pulp.tuples.config.impl;

import sneer.pulp.tuples.config.TupleSpaceConfig;

class TupleSpaceConfigImpl implements TupleSpaceConfig {

	@Override
	public boolean isAcquisitionSynchronous() {
		return true;
	}

}