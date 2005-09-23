//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package wheelexperiments.reactive;

import java.util.HashSet;
import java.util.Set;

public abstract class Notifier<T> implements Signal<T> {

	private final Set<Receiver<T>> _receivers = new HashSet<Receiver<T>>();

	@SuppressWarnings("unchecked")
	public void notifyReceivers() {
		T newValue = currentValue();
		Receiver<T>[] copy;
		synchronized (_receivers) {
			copy = new Receiver[_receivers.size()];
			_receivers.toArray(copy);
		}

		for (Receiver<T> receiver : copy) receiver.receive(newValue);
	}

	public void addReceiver(Receiver<T> receiver) {
		synchronized (_receivers) { _receivers.add(receiver); }
		receiver.receive(currentValue());
	}

	public void removeReceiver(Receiver<T> receiver) {
		synchronized (_receivers) { _receivers.remove(receiver); }
	}
	
}
