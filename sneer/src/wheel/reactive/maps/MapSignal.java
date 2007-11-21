package wheel.reactive.maps;

import wheel.lang.Omnivore;

public interface MapSignal<K,V> {
	
	public void addMapReceiver(Omnivore<MapValueChange<K,V>> receiver);
	public void removeMapReceiver(Omnivore<MapValueChange<K,V>> receiver);

	public V currentGet(K key);
	public int currentSize();

}
