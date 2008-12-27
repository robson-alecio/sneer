package wheel.lang;

import java.util.HashMap;

public class CacheMap<K, V> extends HashMap<K, V> {
	
	public V get(K key, Producer<V> producerToUseIfAbsent) {
		V result = get(key);
		
		if (result == null) {
			result = producerToUseIfAbsent.produce();
			put(key, result);
		}
		
		return result;
	}
	
}
