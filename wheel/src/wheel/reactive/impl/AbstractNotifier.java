package wheel.reactive.impl;

import java.util.ArrayList;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.EventSource;

public abstract class AbstractNotifier<VC> implements EventSource<VC> {

	private final List<ReceiverHolder<Omnivore<VC>>> _receivers = new ArrayList<ReceiverHolder<Omnivore<VC>>>(); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.

	protected void notifyReceivers(VC valueChange) {
		synchronized (_receivers) {
			ReceiverHolder<Omnivore<VC>>[] copyToAvoidConcurrentModificationAsResultOfNotifications = copyOfListeners();
			for (ReceiverHolder<Omnivore<VC>> reference : copyToAvoidConcurrentModificationAsResultOfNotifications)
				notify(reference, valueChange);
		}
	}

	private ReceiverHolder<Omnivore<VC>>[] copyOfListeners() {
		ReceiverHolder<Omnivore<VC>>[] result = createArray(_receivers.size());
		_receivers.toArray(result);
		return result;
	}

	private void notify(ReceiverHolder<Omnivore<VC>> reference, VC valueChange) {
		Omnivore<VC> receiver = reference.get();
		if (receiver == null) {
			System.err.println("Receiver has been garbage collected. (" + reference._alias + ")");
			_receivers.remove(reference);
			return;
		}

		receiver.consume(valueChange);
	}

	@SuppressWarnings("unchecked")
	private ReceiverHolder<Omnivore<VC>>[] createArray(int size) {
		return new ReceiverHolder[size];
	}

	@Override
	public void addReceiver(Omnivore<VC> receiver) {
		synchronized (_receivers) {
			_receivers.add(new ReceiverHolder<Omnivore<VC>>(receiver));
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

class ReceiverHolder<T> extends java.lang.ref.WeakReference<T>{
	
	String _alias;
	
	ReceiverHolder(T receiver) {
		this(receiver, getAlias(receiver));
	}
	
	ReceiverHolder(T receiver, String alias) {
		super(receiver);
		_alias = alias;
	}
	
	static String getAlias(Object object) {
		return "" + object.getClass() + "@" + System.identityHashCode(object);
	}

}