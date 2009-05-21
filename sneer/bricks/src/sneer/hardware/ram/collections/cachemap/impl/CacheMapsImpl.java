package sneer.hardware.ram.collections.cachemap.impl;

import sneer.hardware.ram.collections.cachemap.CacheMap;
import sneer.hardware.ram.collections.cachemap.CacheMaps;

class CacheMapsImpl implements CacheMaps {

	@Override
	public <K, V> CacheMap<K, V> newInstance() {
		return new CacheMapImpl<K, V>();
	}
}
