package sneer.bricks.pulp.reactive.collections.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.MapRegister;
import sneer.bricks.pulp.reactive.collections.MapSignal;
import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.SetSignal;

class MapRegisterImpl<K,V> implements MapRegister<K,V> {
	
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

	private class MyOutput implements MapSignal<K,V> {

		private final EventNotifier<CollectionChange<Map.Entry<K,V>>> _notifier = my(EventNotifiers.class).create(new Consumer<Consumer<? super CollectionChange<Map.Entry<K,V>>>>(){@Override public void consume(Consumer<? super CollectionChange<Entry<K, V>>> newReceiver) {
			if (_map.isEmpty()) return;
			newReceiver.consume(asChange(_map.entrySet()));
		}});

		@Override
		public V currentGet(K key) {
			return _map.get(key);
		}

		@Override
		public void addReceiver(Consumer<? super CollectionChange<Map.Entry<K,V>>> receiver) {
			_notifier.output().addReceiver(receiver);		
		}
		
		@Override
		public void removeReceiver(Object receiver) {
			_notifier.output().removeReceiver(receiver);		
		}

		@Override
		public Collection<Entry<K, V>> currentElements() {
			return new HashSet<Entry<K, V>>(_map.entrySet());
		}

		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return currentElements().iterator();
		}

		@Override
		public SetSignal<K> keys() {
			return _keys.output();
		}


		private CollectionChange<Entry<K, V>> asChange(Collection<Entry<K, V>> entries) {
			return new CollectionChangeImpl<Entry<K, V>>(entries, null);
		}

		
		@Override
		public Signal<Integer> size() {
			throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(); // Implement
		}

		@Override
		public boolean currentContains(Entry<K, V> entry) {
			return _map.entrySet().contains(entry);
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
		
		notifyReceivers(added, removed);
	}


	@Override
	public void remove(K key) {
		V oldValue = _map.remove(key);
		_keys.remove(key);
		
		Entry<K, V> removed = new MyEntry<K,V>(key, oldValue);
		notifyReceivers(null, removed);
	}

	
	private void notifyReceivers(Entry<K, V> added, Entry<K, V> removed) {
		_output._notifier.notifyReceivers(new CollectionChangeImpl<Entry<K,V>>(added, removed));
	}

}
