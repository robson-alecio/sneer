package sneer.bricks.hardware.ram.maps.cachemaps;

import java.util.Map;

import sneer.foundation.commons.lang.Producer;

public interface CacheMap<K, V> extends Map<K, V> {

	V get(K key, Producer<V> producerToUseIfAbsent);

}
