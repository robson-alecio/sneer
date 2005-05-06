//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapHolder<K,V> implements MapView<K,V> {

	private final Map<K, V> _contents = new HashMap<K, V>(); 
	private final SetHolder<K> _keys = new SetHolder<K>(); 
	private final Set<Observer<K,V>> _observers = new HashSet<Observer<K,V>>();
	
	
	private void notifyEntryAddition(K key, V value) {
		Iterator<Observer<K,V>> it = _observers.iterator();
		while (it.hasNext())
			it.next().entryAdded(key, value);
	}

	private void notifyKeyRemoval(K key) {
		Iterator<Observer<K,V>> it = _observers.iterator();
		while (it.hasNext())
			it.next().keyRemoved(key);
	}

	public synchronized void addObserver(Observer<K,V> observer) {
		_observers.add(observer);
	}

	public void put(K key, V value) {
		_keys.add(key);
		_contents.put(key, value);
		notifyEntryAddition(key, value);
	}

	public void remove(K key) {
		_contents.remove(key);
		notifyKeyRemoval(key);
	}

	public SetView<K> keys() {
		return _keys;
	}

	public boolean containsKey(K key) {
		return _keys.contains(key);
	}

	public Object get(K key) {
		return _contents.get(key);
	}

	public Map<K, V> currentValue() {
		return new HashMap<K, V>(_contents);
	}

}
