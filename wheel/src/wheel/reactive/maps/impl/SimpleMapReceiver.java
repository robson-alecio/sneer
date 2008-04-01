package wheel.reactive.maps.impl;

import wheel.reactive.maps.MapSignal;

public class SimpleMapReceiver<K, V> extends VisitingMapReceiver<K, V> {

	private final MapSignal<K, V> _mapSignal;
	
	public SimpleMapReceiver(MapSignal<K, V> mapSignal) {
		_mapSignal = mapSignal;
	}
	
	@Override
	public void elementAdded(K key, V value) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void elementRemoved(K key, V value) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void elementReplaced(K key, V value) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void elementToBeRemoved(K key, V value) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void elementToBeReplaced(K key, V value) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}


}
