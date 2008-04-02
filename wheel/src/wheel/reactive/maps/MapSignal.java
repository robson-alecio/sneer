package wheel.reactive.maps;

import java.util.Set;

import wheel.lang.Omnivore;

public interface MapSignal<K,V> {
	
	void addMapReceiver(Omnivore<MapValueChange<K,V>> receiver);
	void removeMapReceiver(Omnivore<MapValueChange<K,V>> receiver);

	
	Set<K> currentKeys();
	V currentGet(K key);
	int currentSize();

}
