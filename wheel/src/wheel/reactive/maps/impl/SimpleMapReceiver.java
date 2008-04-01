package wheel.reactive.maps.impl;

import wheel.reactive.maps.MapSignal;

public abstract class SimpleMapReceiver<K, V> extends VisitingMapReceiver<K, V> {

	private final MapSignal<K, V> _mapSignal;
	
	public SimpleMapReceiver(MapSignal<K, V> mapSignal) {
		_mapSignal = mapSignal;
	}

	@Override
	public void elementToBeRemoved(K key, V value) {
	}

	@Override
	public void elementToBeReplaced(K key, V value) {
	}
}
