//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz.

package views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetHolder implements SetView {

	private Set _contents = new HashSet();

	private final Set _observers = new HashSet();

	public boolean contains(Object key) {
		return _contents.contains(key);
	}

	public void add(Object key) {
		_contents.add(key);
		notifyObservers(key);
	}

	private void notifyObservers(Object key) {
		Iterator it = _observers.iterator();
		while (it.hasNext()) notifyObserver((Observer)it.next(), key);
	}

	private void notifyObserver(Observer observer, Object key) {
		observer.elementAdded(key);
	}

	public synchronized void addObserver(Observer observer) {
		_observers.add(observer);
	}

	
}
