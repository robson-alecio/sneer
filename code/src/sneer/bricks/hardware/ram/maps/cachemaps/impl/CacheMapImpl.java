package sneer.bricks.hardware.ram.maps.cachemaps.impl;

import java.util.HashMap;

import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.foundation.lang.Producer;

class CacheMapImpl<K, V> extends HashMap<K, V> implements CacheMap<K, V> {

	@Override
	public V get(K key, Producer<V> producerToUseIfAbsent) {
		V result = get(key);
		
		if (result == null) {
			result = producerToUseIfAbsent.produce();
			put(key, result);
		}
		
		return result;
	}
}
