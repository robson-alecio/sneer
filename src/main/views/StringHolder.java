//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class StringHolder implements StringView {

	private String _contents;
	private final Set _observers = new HashSet();

	
	public synchronized void hold(String newContents) {
		_contents = newContents;
		notifyObservers();
	}

	private void notifyObservers() {
		Iterator it = _observers.iterator();
		while (it.hasNext()) notifyObserver((Observer)it.next());
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
