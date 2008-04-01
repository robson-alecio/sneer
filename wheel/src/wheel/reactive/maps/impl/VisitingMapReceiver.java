package wheel.reactive.maps.impl;

import wheel.lang.Omnivore;
import wheel.reactive.maps.MapValueChange;
import wheel.reactive.maps.MapValueChange.Visitor;

public abstract class VisitingMapReceiver<K, V> implements Omnivore<MapValueChange<K, V>>, Visitor<K, V>{

	@Override
	public void consume(MapValueChange<K, V>mapChange) {
		mapChange.accept(this);
	}

}
