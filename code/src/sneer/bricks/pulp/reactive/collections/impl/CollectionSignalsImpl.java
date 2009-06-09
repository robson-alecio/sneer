package sneer.bricks.pulp.reactive.collections.impl;

import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.MapRegister;

class CollectionSignalsImpl implements CollectionSignals {

	@Override
	public <K, V> MapRegister<K, V> newMapRegister() {
		throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public <T> ListRegister<T> newListRegister() {
		return new ListRegisterImpl<T>();
	}

}
