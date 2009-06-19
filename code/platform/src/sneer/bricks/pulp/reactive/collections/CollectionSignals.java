package sneer.bricks.pulp.reactive.collections;

import sneer.foundation.brickness.Brick;

@Brick
public interface CollectionSignals {

	<T> ListRegister<T> newListRegister();
	<K, V> MapRegister<K, V> newMapRegister();
	
}
