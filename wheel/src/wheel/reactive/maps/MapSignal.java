package wheel.reactive.maps;

import wheel.lang.Pair;
import wheel.reactive.sets.SetSignal;

public interface MapSignal<K,V> extends SetSignal<Pair<K, V>> {
	
	SetSignal<K> keys();
	
	V currentGet(K key);

}
