package sneer.bricks.hardware.ram.maps.cachemaps.impl;

import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;

class CacheMapsImpl implements CacheMaps {

	@Override
	public <K, V> CacheMap<K, V> newInstance() {
		return new CacheMapImpl<K, V>();
	}
}
