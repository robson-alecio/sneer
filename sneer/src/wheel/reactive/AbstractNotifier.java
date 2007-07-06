package wheel.reactive;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import wheel.lang.Omnivore;

public abstract class AbstractNotifier<VC> {

	private final Set<Omnivore<VC>> _receivers = new HashSet<Omnivore<VC>>(); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.  
	private transient Set<Omnivore<VC>> _transientReceivers;
	private final Object _monitor = new Object();

	protected void notifyReceivers(VC valueChange) {
		synchronized (_monitor) {
			notify(_receivers, valueChange);
			if (_transientReceivers != null)
				notify(_transientReceivers, valueChange);
		}
	}

	@SuppressWarnings("unchecked")
	private void notify(Set<Omnivore<VC>> receivers, VC valueChange) {
		Omnivore<VC>[] copy;
		copy = new Omnivore[receivers.size()];
		receivers.toArray(copy);
	
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
			boolean wasThere = _receivers.remove(receiver);
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
		if (_transientReceivers == null) _transientReceivers = new HashSet<Omnivore<VC>>();  
		return _transientReceivers;
	}

}
