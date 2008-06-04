package wheel.reactive.maps.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.Wrapper;
import wheel.reactive.maps.MapRegister;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.SetSignal.SetValueChange;
import wheel.reactive.sets.impl.SetValueChangeImpl;

public class MapRegisterImpl<K,V> implements MapRegister<K,V> {
	
	private class MyOutput implements MapSignal<K,V> {

		@Override
		public V currentGet(K key) {
			return _map.get(key);
		}

		@Override
		public int currentSize() {
			return _map.size();
		}

		@Override
		public void addSetReceiver(Omnivore<SetValueChange<Pair<K,V>>> receiver) {
			_keys.output().addSetReceiver(new MapReceiverAdapter(receiver));		
		}
		
		@Override
		public void removeSetReceiver(Object receiver) {
			_keys.output().removeSetReceiver(receiver);		
		}


		@Override
		public Collection<Pair<K, V>> currentElements() {
			return convertToMapEntries(keys().currentElements());
		}

		@Override
		public Iterator<Pair<K, V>> iterator() {
			return currentElements().iterator();
		}

		@Override
		public SetSignal<K> keys() {
			return _keys.output();
		}


	}

	private final Map<K,V> _map = new ConcurrentHashMap<K,V>();
	private final SetRegister<K> _keys = new SetRegister<K>();
	
	private MyOutput _output = new MyOutput();

	
	@Override
	public MapSignal<K,V> output() {
		return _output;
	}
	
	public void put(K key, V value) {
		V old = _map.get(key);
		if (old != null) remove(key);
		System.err.println("This sould happen in a single notification..."); //Fix
		
		_map.put(key, value);
		_keys.add(key);
	}
	
	@Override
	public void remove(K key) {
		_map.remove(key);
		_keys.remove(key);
	}

	private Collection<Pair<K, V>> convertToMapEntries(Collection<K> keys) {
		Collection<Pair<K,V>> result = new ArrayList<Pair<K,V>>(keys.size());
		for(K key : keys)
			result.add(new Pair<K,V>(key, _map.get(key)));
		return result;
	}
	
	
	private class MapReceiverAdapter extends Wrapper<Omnivore<SetValueChange<Pair<K,V>>>> implements Omnivore<SetValueChange<K>> {
		
		private MapReceiverAdapter(Omnivore<SetValueChange<Pair<K, V>>> receiver) {
			super(receiver);
		}
		
		@Override
		public void consume(SetValueChange<K> change) {
			Collection<K> keysAdded = change.elementsAdded();
			Collection<K> keysRemoved = change.elementsRemoved();

			Collection<Pair<K, V>> added = convertToMapEntries(keysAdded);
			Collection<Pair<K, V>> removed = convertToMapEntries(keysRemoved);
				
			_delegate.consume(new SetValueChangeImpl<Pair<K,V>>(added, removed));
		}

	}

}
