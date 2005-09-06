//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package wheelexperiments.reactive.signals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



public class StringSource implements StringSignal {

	private String _contents;
	private final Set<Observer> _observers = new HashSet<Observer>();

	
	public synchronized void hold(String newContents) {
		_contents = newContents;
		notifyObservers();
	}

	private void notifyObservers() {
		Iterator<Observer> it = _observers.iterator();
		while (it.hasNext()) notifyObserver(it.next());
	}

	private void notifyObserver(Observer observer) {
		observer.observeChange(_contents);
	}

	public synchronized void addObserver(Observer observer) {
		_observers.add(observer);
		notifyObserver(observer);
	}

	public String currentValue() {
		return _contents;
	}
	
}
