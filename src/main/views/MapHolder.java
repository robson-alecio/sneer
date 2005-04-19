//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz.

package views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapHolder implements MapView {

	private final Map _contents = new HashMap(); 
	private final SetHolder _keys = new SetHolder(); 
	private final Set _observers = new HashSet();
	
	
	private void notifyEntryAddition(Object key, Object value) {
		Iterator it = _observers.iterator();
		while (it.hasNext())
			((Observer)it.next()).entryAdded(key, value);
	}

	private void notifyKeyRemoval(Object key) {
		Iterator it = _observers.iterator();
		while (it.hasNext())
			((Observer)it.next()).keyRemoved(key);
	}

	public synchronized void addObserver(Observer observer) {
		_observers.add(observer);
	}

	public void put(Object key, Object value) {
		_keys.add(key);
		_contents.put(key, value);
		notifyEntryAddition(key, value);
	}

	public void remove(Object key) {
		_contents.remove(key);
		notifyKeyRemoval(key);
	}

	public SetView keys() {
		return _keys;
	}

	public boolean containsKey(String nickname) {
		return _keys.contains(nickname);
	}

	public Object get(Object key) {
		return _contents.get(key);
	}

	public Map currentValue() {
		return new HashMap(_contents);
	}

}
