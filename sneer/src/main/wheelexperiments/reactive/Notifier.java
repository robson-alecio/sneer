//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package wheelexperiments.reactive;

import java.util.HashSet;
import java.util.Set;

public abstract class Notifier<T> implements Signal<T> {

	private final Set<Receiver<T>> _receivers = new HashSet<Receiver<T>>();

	public synchronized void notifyReceivers() {
		T newValue = currentValue();
		for (Receiver<T> receiver : _receivers) {
			receiver.receive(newValue);
		}
	}

	public synchronized void addReceiver(Receiver<T> receiver) {
		_receivers.add(receiver);
		receiver.receive(currentValue());
	}
	
}
