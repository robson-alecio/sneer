package wheel.reactive.maps.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.impl.AbstractNotifier;
import wheel.reactive.maps.MapRegister;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.SetSignal.SetValueChange;
import wheel.reactive.sets.impl.SetRegisterImpl;
import wheel.reactive.sets.impl.SetValueChangeImpl;

public class MapRegisterImpl<K,V> implements MapRegister<K,V> {
	
	static class MyEntry<K,V> implements Entry<K, V> {

		private final K _key;
		private final V _value;

		private MyEntry(K key, V value) {
			_key = key;
			_value = value;
		}

		@Override
		public K getKey() {
			return _key;
		}

		@Override
		public V getValue() {
			return _value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}

	}


	private class MyOutput extends AbstractNotifier<SetValueChange<Map.Entry<K,V>>> implements MapSignal<K,V> {

		@Override
		public V currentGet(K key) {
			return _map.get(key);
		}

		@Override
		public int currentSize() {
			return _map.size();
		}

		@Override
		public void addSetReceiver(Consumer<SetValueChange<Map.Entry<K,V>>> receiver) {
			addReceiver(receiver);		
		}
		
		@Override
		public void removeSetReceiver(Object receiver) {
			removeReceiver(receiver);		
		}


		@Override
		public Collection<Map.Entry<K, V>> currentElements() {
			return new ArrayList<Map.Entry<K, V>>(_map.entrySet());
		}

		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return currentElements().iterator();
		}

		@Override
		public SetSignal<K> keys() {
			return _keys.output();
		}

		@Override
		protected void initReceiver(Consumer<? super SetValueChange<Map.Entry<K, V>>> receiver) {
			if (_map.isEmpty()) return;
			receiver.consume(asChange(currentElements()));
		}

		private SetValueChange<Entry<K, V>> asChange(Collection<Entry<K, V>> entries) {
			return new SetValueChangeImpl<Entry<K, V>>(entries, null);
		}

		@Override
		protected void notifyReceivers(SetValueChange<Entry<K, V>> change) {
			super.notifyReceivers(change);
		}

		@Override
		public Signal<Integer> size() {
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		}


	}

	private final Map<K,V> _map = new ConcurrentHashMap<K,V>();
	private final SetRegister<K> _keys = new SetRegisterImpl<K>(); //Optimize This is redundant with the keys in the map.
	
	private MyOutput _output = new MyOutput();

	
	@Override
	public MapSignal<K,V> output() {
		return _output;
	}
	
	@Override
	synchronized public void put(K key, V value) {
		boolean isNewKey = !_map.containsKey(key);
		
		V oldValue = _map.put(key, value);
		if (isNewKey) _keys.add(key);

		Entry<K, V> added = new MyEntry<K,V>(key, value);
		Entry<K, V> removed = isNewKey
			? null
			: new MyEntry<K,V>(key, oldValue);
		
		SetValueChange<Entry<K, V>> change = new SetValueChangeImpl<Entry<K,V>>(added, removed);
		_output.notifyReceivers(change);
	}


	@Override
	public void remove(K key) {
		V oldValue = _map.remove(key);
		_keys.remove(key);
		
		Entry<K, V> removed = new MyEntry<K,V>(key, oldValue);
		_output.notifyReceivers(new SetValueChangeImpl<Entry<K,V>>(null, removed));
	}

}
