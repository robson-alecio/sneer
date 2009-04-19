package sneer.pulp.events.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.commons.environments.Environments;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventSource;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import wheel.lang.Types;

class EventNotifierImpl<T> implements EventNotifier<T>, EventSource<T> {

	private static final ReceiverHolder<?>[] RECEIVER_HOLDER_ARRAY_TYPE = new ReceiverHolder[0];
	
	private final List<ReceiverHolder<Consumer<? super T>>> _receivers = Collections.synchronizedList(new ArrayList<ReceiverHolder<Consumer<? super T>>>()); //Fix: Potential object leak. Receivers must be weak referenced. This is equivalent to the whiteboard pattern too, from a receiver referencing perspective. Conceptually, it is only the receiver that references the signal.

	private final Consumer<Consumer<? super T>> _receiverHandler;

	EventNotifierImpl(Consumer<Consumer<? super T>> receiverHandler) {
		_receiverHandler = receiverHandler;
	}

	@Override
	public void notifyReceivers(T valueChange) {
		ReceiverHolder<Consumer<T>>[] receivers = copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications();
		for (ReceiverHolder<Consumer<T>> reference : receivers)
			notify(reference, valueChange);
	}

	private ReceiverHolder<Consumer<T>>[] copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications() {
		return (ReceiverHolder<Consumer<T>>[]) _receivers.toArray(RECEIVER_HOLDER_ARRAY_TYPE);
	}

	private void notify(ReceiverHolder<Consumer<T>> reference, T valueChange) {
		Consumer<T> receiver = reference.get();
		if (receiver == null) {
			my(Logger.class).log("Receiver has been garbage collected. ({})", reference._alias);
			_receivers.remove(reference);
			return;
		}

		receiver.consume(valueChange);
	}

	@Override
	public void addReceiver(final Consumer<? super T> receiver) {
		_receivers.add(holderFor(receiver));
		Environments.my(ExceptionHandler.class).shield(new Runnable() { @Override public void run() {
			if (_receiverHandler == null) return;
			_receiverHandler.consume(receiver);
		}});
	}

	@Override
	public void removeReceiver(Object receiver) {
		final Consumer<? super T> typedReceiver = Types.cast(receiver);
		boolean wasThere = _receivers.remove(holderFor(typedReceiver)); //Optimize consider a Set for when there is a great number of receivers.
		assert wasThere;
	}

	@Override
	protected void finalize() throws Throwable {
		ReceiverHolder<Consumer<T>>[] receivers = copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications();
		if(receivers.length != 0)
			my(Logger.class).log(debugMessage(receivers));
	}

	private String debugMessage(ReceiverHolder<Consumer<T>>[] receivers) {
		StringBuilder result = new StringBuilder();
		result.append("Abstract notifier finalized.\n");
		
		for (ReceiverHolder<Consumer<T>> reference : receivers)
			result.append("\tReceiver: " + reference._alias + "\n");
		
		return result.toString();
	}
	
	private ReceiverHolder<Consumer<? super T>> holderFor(
			Consumer<? super T> receiver) {
		return new ReceiverHolder<Consumer<? super T>>(receiver);
	}

	@Override
	public EventSource<T> output() {
		return this;
	}
}

