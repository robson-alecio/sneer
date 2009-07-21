package sneer.bricks.hardware.ram.maps.cachemaps.impl;

import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.foundation.lang.Producer;

class CacheMapImpl<K, V> extends ConcurrentHashMap<K, V> implements CacheMap<K, V> {

	@Override
	synchronized public V get(K key, Producer<V> producerToUseIfAbsent) {
		V result = get(key);
		
		if (result == null) {
			result = producerToUseIfAbsent.produce();
			put(key, result);
		}
		
		return result;
	}
}
