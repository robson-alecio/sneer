package sneer.hardware.ram.collections.cachemap.impl;

import java.util.HashMap;

import sneer.commons.lang.Producer;
import sneer.hardware.ram.collections.cachemap.CacheMap;

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
