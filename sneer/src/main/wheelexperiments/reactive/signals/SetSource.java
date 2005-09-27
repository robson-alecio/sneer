//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheelexperiments.reactive.signals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class SetSource<T> implements SetSignal<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private Set<T> _contents = new HashSet<T>();

	private final Set<Receiver<T>> _receivers = new HashSet<Receiver<T>>();

	public void add(T element) {
		synchronized (_contents) {
			_contents.add(element);
		}
		notifyOfAddition(element);
	}

	public void remove(T element) {
		synchronized (_contents) {
			_contents.remove(element);
		}
		notifyOfRemoval(element);
	}

	private void notifyOfRemoval(T element) {
		for (Receiver<T> item : receiversCopy()) {
			item.elementRemoved(element);
		}
	}
	
	private void notifyOfAddition(T element) {
		for (Receiver<T> item : receiversCopy()) {
			item.elementAdded(element);
		}
	}

	public synchronized void addReceiver(Receiver<T> receiver) {
		_receivers.add(receiver);
		for (T element : _contents)
			notifyOfAddition(element);
	}

	public synchronized void removeReceiver(Receiver<T> receiver) {
		_receivers.remove(receiver);
	}
	
	private synchronized Iterable<Receiver<T>> receiversCopy() {
		return new ArrayList<Receiver<T>>(_receivers);
	}

	public Set<T> currentValue() {
		synchronized (_contents) {
			return new HashSet<T>(_contents);
		}
	}

}
