package wheel.reactive.impl;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.EventSource;

public abstract class AbstractNotifier<VC> implements EventSource<VC> {

	private final List<WeakReference<Omnivore<VC>>> _receivers = new ArrayList<WeakReference<Omnivore<VC>>>(); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.

	protected void notifyReceivers(VC valueChange) {
		synchronized (_receivers) {
			WeakReference<Omnivore<VC>>[] copyToAvoidConcurrentModificationAsResultOfNotifications = copyOfListeners();
			for (WeakReference<Omnivore<VC>> reference : copyToAvoidConcurrentModificationAsResultOfNotifications)
				notify(reference, valueChange);
		}
	}

	private WeakReference<Omnivore<VC>>[] copyOfListeners() {
		WeakReference<Omnivore<VC>>[] result = createArray(_receivers.size());
		_receivers.toArray(result);
		return result;
	}

	private void notify(WeakReference<Omnivore<VC>> reference, VC valueChange) {
		Omnivore<VC> receiver = reference.get();
		if (receiver == null) {
			//Fix: Remove this empty WeakReference from the listeners list.
			System.out.println("Receiver has been garbage collected");
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
		synchronized (_receivers) {
			_receivers.add(new WeakReference<Omnivore<VC>>(receiver));
			initReceiver(receiver);
		}
	}

	protected abstract void initReceiver(Omnivore<VC> receiver);
	
	@Override
	public void removeReceiver(Object receiver) {
		synchronized (_receivers) {
			boolean wasThere = _receivers.remove(receiver); //Optimize consider a Set for when there is a great number of receivers.
			assert wasThere;
		}
	}

}
