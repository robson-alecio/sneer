package sneer.pulp.reactive.collections.impl;

import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.CollectionSignals;
import sneer.pulp.reactive.collections.MapRegister;

class ReactiveCollectionsImpl implements CollectionSignals {

	@Override
	public <K, V> MapRegister<K, V> newMapRegister() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public <T> ListRegister<T> newListRegister() {
		return new ListRegisterImpl<T>();
	}

}
