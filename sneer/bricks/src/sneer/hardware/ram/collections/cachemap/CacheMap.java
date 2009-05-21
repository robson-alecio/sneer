package sneer.hardware.ram.collections.cachemap;

import java.util.Map;

import sneer.commons.lang.Producer;

public interface CacheMap<K, V> extends Map<K, V> {

	V get(K key, Producer<V> producerToUseIfAbsent);

}
