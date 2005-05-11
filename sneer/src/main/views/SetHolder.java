//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetHolder<T> implements SetView<T> {

	private Set<T> _contents = new HashSet<T>();

	private final Set<Observer<T>> _observers = new HashSet<Observer<T>>();

	public boolean contains(T key) {
		return _contents.contains(key);
	}

	public void add(T key) {
		_contents.add(key);
		notifyObservers(key);
	}

	private void notifyObservers(T key) {
		Iterator<Observer<T>> it = _observers.iterator();
		while (it.hasNext()) notifyObserver(it.next(), key);
	}

	private void notifyObserver(Observer<T> observer, T key) {
		observer.elementAdded(key);
	}

	public synchronized void addObserver(Observer<T> observer) {
		_observers.add(observer);
	}

	
}
