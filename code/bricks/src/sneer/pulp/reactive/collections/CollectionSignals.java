package sneer.pulp.reactive.collections;

import sneer.brickness.Brick;

@Brick
public interface CollectionSignals {

	<T> ListRegister<T> newListRegister();
	<K, V> MapRegister<K, V> newMapRegister();
	
}
