package wheel.reactive.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static wheel.io.Logger.log;
import wheel.lang.Omnivore;
import wheel.lang.Types;
import wheel.reactive.EventSource;

public abstract class AbstractNotifier<VC> implements EventSource<VC> {

	private static final ReceiverHolder<?>[] RECEIVER_HOLDER_ARRAY_TYPE = new ReceiverHolder[0];

	
	private final List<ReceiverHolder<Omnivore<? super VC>>> _receivers = Collections.synchronizedList(new ArrayList<ReceiverHolder<Omnivore<? super VC>>>()); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.

	protected void notifyReceivers(VC valueChange) {
		ReceiverHolder<Omnivore<VC>>[] receivers = copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications();
		for (ReceiverHolder<Omnivore<VC>> reference : receivers)
			notify(reference, valueChange);
	}

	private ReceiverHolder<Omnivore<VC>>[] copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications() {
		return (ReceiverHolder<Omnivore<VC>>[]) _receivers.toArray(RECEIVER_HOLDER_ARRAY_TYPE);
	}

	private void notify(ReceiverHolder<Omnivore<VC>> reference, VC valueChange) {
		Omnivore<VC> receiver = reference.get();
		if (receiver == null) {
			log("Receiver has been garbage collected. ({})", reference._alias);
			_receivers.remove(reference);
			return;
		}

		receiver.consume(valueChange);
	}

	@Override
	public void addReceiver(Omnivore<? super VC> receiver) {
		_receivers.add(holderFor(receiver));
		initReceiver(receiver);
	}

	protected abstract void initReceiver(Omnivore<? super VC> receiver);
	
	@Override
	public void removeReceiver(Object receiver) {
		final Omnivore<? super VC> typedReceiver = Types.cast(receiver);
		boolean wasThere = _receivers.remove(holderFor(typedReceiver)); //Optimize consider a Set for when there is a great number of receivers.
		assert wasThere;
	}

	@Override
	protected void finalize() throws Throwable {
		ReceiverHolder<Omnivore<VC>>[] receivers = copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications();
		if(receivers.length != 0)
			log(debugMessage(receivers));
	}

	private String debugMessage(ReceiverHolder<Omnivore<VC>>[] receivers) {
		StringBuilder result = new StringBuilder();
		result.append("Abstract notifier finalized.\n");
		
		for (ReceiverHolder<Omnivore<VC>> reference : receivers)
			result.append("\tReceiver: " + reference._alias + "\n");
		
		return result.toString();
	}
	
	private ReceiverHolder<Omnivore<? super VC>> holderFor(
			Omnivore<? super VC> receiver) {
		return new ReceiverHolder<Omnivore<? super VC>>(receiver);
	}
}

class ReceiverHolder<T> extends java.lang.ref.WeakReference<T>{
	
	final String _alias;
	
	ReceiverHolder(T receiver) {
		this(receiver, getAlias(receiver));
	}
	
	ReceiverHolder(T receiver, String alias) {
		super(receiver);
		_alias = alias;
	}
	
	@Override
	public String toString() {
		return _alias;
	}
	
	@Override
	public boolean equals(Object obj) {
		final ReceiverHolder<T> holder = Types.cast(obj);
		return holder.get() == get();
	}
	
	static String getAlias(Object object) {
		return "" + object.getClass() + "@" + System.identityHashCode(object);
		//USE object.toString() HERE ONLY FORDEBUGGING. DO NOT COMMIT.
	}

}