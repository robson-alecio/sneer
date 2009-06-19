package sneer.bricks.pulp.datastructures.cache.impl;

import sneer.bricks.pulp.datastructures.cache.Cache;
import sneer.bricks.pulp.datastructures.cache.CacheFactory;

class CacheFactoryImpl implements CacheFactory {

	@Override
	public <T> Cache<T> createWithCapacity(int capacity) {
		return new CacheImpl<T>(capacity);
	}

}