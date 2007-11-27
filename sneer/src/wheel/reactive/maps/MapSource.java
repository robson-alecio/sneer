package wheel.reactive.maps;

import java.io.Serializable;
import java.util.Set;

import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface MapSource<K, V> extends Serializable{

	MapSignal<K, V> output();

	void put(K key, V value);
	V get(K key);

	Omnivore<Pair<K, V>> setter();

	boolean remove(K key);

	Set<K> keys();

}