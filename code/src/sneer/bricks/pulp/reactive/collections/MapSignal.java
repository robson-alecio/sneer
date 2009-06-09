package sneer.bricks.pulp.reactive.collections;

import java.util.Map;


public interface MapSignal<K,V> extends SetSignal<Map.Entry<K, V>> {
	
	SetSignal<K> keys();
	
	V currentGet(K key);

}
