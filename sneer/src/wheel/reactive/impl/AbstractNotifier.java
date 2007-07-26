package wheel.reactive.impl;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wheel.lang.Omnivore;

public abstract class AbstractNotifier<VC> {

	private final List<Omnivore<VC>> _receivers = new LinkedList<Omnivore<VC>>(); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.
	private transient List<Omnivore<VC>> _transientReceivers;
	private final Object _monitor = new Object();

	protected void notifyReceivers(VC valueChange) {
		synchronized (_monitor) {
			notify(_receivers, valueChange);
			if (_transientReceivers != null)
				notify(_transientReceivers, valueChange);
		}
	}

	@SuppressWarnings("unchecked")
	private void notify(List<Omnivore<VC>> _receivers2, VC valueChange) {
		Omnivore<VC>[] copy;
		copy = new Omnivore[_receivers2.size()];
		_receivers2.toArray(copy);
	
		for (Omnivore<VC> receiver : copy) receiver.consume(valueChange);
	}

	public void addReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			boolean isNew = _receivers.add(receiver);
			assert isNew;
			initReceiver(receiver);
		}
	}

	protected abstract void initReceiver(Omnivore<VC> receiver);
	
	public void removeReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			boolean wasThere = _receivers.remove(receiver); //Optimize: List has linear lookup time. Cannot simply replace for a Set because receivers must be notified in the order the registered.
			assert wasThere;
		}
	}

	public void addTransientReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			boolean isNew = transientReceivers().add(receiver);
			assert isNew;
			initReceiver(receiver);
		}
	}

	public void removeTransientReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			boolean wasThere = transientReceivers().remove(receiver);
			assert wasThere;
		}
	}

	private Collection<Omnivore<VC>> transientReceivers() {
		if (_transientReceivers == null) _transientReceivers = new LinkedList<Omnivore<VC>>();  
		return _transientReceivers;
	}

}
