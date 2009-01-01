package sneer.pulp.datastructures.cache.impl;

import sneer.pulp.datastructures.cache.Cache;
import sneer.pulp.datastructures.cache.CacheFactory;

class CacheFactoryImpl implements CacheFactory {

	@Override
	public <T> Cache<T> createWithCapacity(int capacity) {
		return new CacheImpl<T>(capacity);
	}

}