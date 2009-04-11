package sneer.pulp.reactive.collections;

import java.util.Map;

import wheel.reactive.sets.SetSignal;

public interface MapSignal<K,V> extends SetSignal<Map.Entry<K, V>> {
	
	SetSignal<K> keys();
	
	V currentGet(K key);

}
