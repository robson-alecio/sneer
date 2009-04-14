package sneer.pulp.reactive.collections.impl;

import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.pulp.reactive.collections.MapRegister;

class ReactiveCollectionsImpl implements ReactiveCollections {

	@Override
	public <K, V> MapRegister<K, V> newMapRegister() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public <T> ListRegister<T> newListRegister() {
		return new ListRegisterImpl<T>();
	}

}
