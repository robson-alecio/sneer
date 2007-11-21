package wheel.reactive.maps;

import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface MapSource<K,V> {

	MapSignal<K,V> output();

	void put(K key, V value);
	Omnivore<Pair<K,V>> setter();

	boolean remove(K key);
	
}