package wheel.reactive.maps.impl;

import wheel.reactive.maps.MapSignal;

public abstract class SimpleMapReceiver<K, V> extends VisitingMapReceiver<K, V> {

	private final MapSignal<K, V> _mapSignal;
	
	public SimpleMapReceiver(MapSignal<K, V> mapSignal) {
		_mapSignal = mapSignal;
		for (K key : _mapSignal.currentKeys()) entryPresent(key, _mapSignal.currentGet(key));
		_mapSignal.addMapReceiver(this);
	}

	public void crash() {
		_mapSignal.removeMapReceiver(this);
	}

	protected abstract void entryPresent(K key, V currentGet);


	@Override
	public void entryToBeReplaced(K key, V value) {
		entryToBeRemoved(key, value);
	}

	@Override
	public void entryReplaced(K key, V value) {
		entryAdded(key, value);
	}

	@Override
	public void entryRemoved(K key, V value) {}
	
}
