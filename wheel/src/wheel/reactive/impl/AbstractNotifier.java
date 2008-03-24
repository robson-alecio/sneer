package wheel.reactive.impl;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.EventSource;

public abstract class AbstractNotifier<VC> implements EventSource<VC> {

	private final List<WeakReference<Omnivore<VC>>> _receivers = new ArrayList<WeakReference<Omnivore<VC>>>(); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.
	private transient List<WeakReference<Omnivore<VC>>> _transientReceivers;
	private final Object _monitor = new Object();

	protected void notifyReceivers(VC valueChange) {
		synchronized (_monitor) {
			notify(_receivers, valueChange);
			if (_transientReceivers != null)
				notify(_transientReceivers, valueChange);
		}
	}

	private void notify(List<WeakReference<Omnivore<VC>>> receivers, VC valueChange) {
		WeakReference<Omnivore<VC>>[] copy = createArray(receivers.size());
		receivers.toArray(copy);
	
		for (WeakReference<Omnivore<VC>> reference : copy)
			notify(reference, valueChange);
	}

	private void notify(WeakReference<Omnivore<VC>> reference, VC valueChange) {
		Omnivore<VC> receiver = reference.get();
		if (receiver == null) {
			//Fix: Remove this empty WeakReference form the listeners or transientListeners list.
			return;
		}

		receiver.consume(valueChange);
	}

	@SuppressWarnings("unchecked")
	private WeakReference<Omnivore<VC>>[] createArray(int size) {
		return new WeakReference[size];
	}

	@Override
	public void addReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			_receivers.add(new WeakReference<Omnivore<VC>>(receiver));
			initReceiver(receiver);
		}
	}

	protected abstract void initReceiver(Omnivore<VC> receiver);
	
	@Override
	public void removeReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			boolean wasThere = _receivers.remove(receiver); //Optimize: List has linear lookup time. Cannot simply replace for a Set because receivers must be notified in the order the registered.
			assert wasThere;
		}
	}

	@Override
	public void addTransientReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			transientReceivers().add(new WeakReference<Omnivore<VC>>(receiver));
			initReceiver(receiver);
		}
	}

	@Override
	public void removeTransientReceiver(Omnivore<VC> receiver) {
		synchronized (_monitor) {
			boolean wasThere = transientReceivers().remove(receiver);
			assert wasThere;
		}
	}

	private Collection<WeakReference<Omnivore<VC>>> transientReceivers() {
		if (_transientReceivers == null)
			_transientReceivers = new LinkedList<WeakReference<Omnivore<VC>>>();  
		return _transientReceivers;
	}

}
