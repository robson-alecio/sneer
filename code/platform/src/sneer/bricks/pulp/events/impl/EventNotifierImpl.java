package sneer.bricks.pulp.events.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Consumer;

class EventNotifierImpl<T> implements EventNotifier<T>, EventSource<T> {

	private static final WeakRefWithAlias<?>[] RECEIVER_HOLDER_ARRAY_TYPE = new WeakRefWithAlias[0];
	
	private final List<WeakRefWithAlias<Consumer<? super T>>> _receivers = Collections.synchronizedList(new ArrayList<WeakRefWithAlias<Consumer<? super T>>>());

	private final Consumer<Consumer<? super T>> _receiverHandler;

	EventNotifierImpl() {
		this(null);
	}

	EventNotifierImpl(Consumer<Consumer<? super T>> receiverHandler) {
		_receiverHandler = receiverHandler;
	}

	@Override
	public void notifyReceivers(T valueChange) {
		WeakRefWithAlias<Consumer<T>>[] receivers = copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications();
		for (WeakRefWithAlias<Consumer<T>> reference : receivers)
			notify(reference, valueChange);
	}

	private WeakRefWithAlias<Consumer<T>>[] copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications() {
		return (WeakRefWithAlias<Consumer<T>>[]) _receivers.toArray(RECEIVER_HOLDER_ARRAY_TYPE);
	}

	private void notify(WeakRefWithAlias<Consumer<T>> reference, T valueChange) {
		Consumer<T> receiver = reference.get();
		if (receiver == null) {
			my(Logger.class).log("Receiver has been garbage collected. ({})", reference._alias);
			_receivers.remove(reference);
			return;
		}

		receiver.consume(valueChange);
	}

	@Override
	public WeakContract addPulseReceiver(final Runnable pulseReceiver) {
		return addReceiver(new Consumer<Object>() { @Override public void consume(Object ignored) {
			pulseReceiver.run();
		}});
	}

//	@Override
//	protected void finalize() throws Throwable {
//		ReceiverHolder<Consumer<T>>[] receivers = copyOfReceiversToAvoidConcurrentModificationAsResultOfNotifications();
//		if(receivers.length != 0) {
//			my(sneer.pulp.log.Logger.class).log(debugMessage(receivers));
//		}
//	}
//
//	private String debugMessage(ReceiverHolder<Consumer<T>>[] receivers) {
//		StringBuilder result = new StringBuilder();
//		result.append("Abstract notifier finalized.\n");
//		
//		for (ReceiverHolder<Consumer<T>> reference : receivers)
//			result.append("\tReceiver: " + reference._alias + "\n");
//		
//		return result.toString();
//	}
	
	@Override
	public EventSource<T> output() {
		return this;
	}

	@Override
	public WeakContract addReceiver(final Consumer<? super T> eventReceiver) {
		_receivers.add(weakRefFor(eventReceiver));
		handleNewReceiver(eventReceiver); //Fix: this is a potential inconsistency. The receiver might be notified before the handler can do its thing. Reversing the two lines can cause the receiver to lose events. Some sort of synchronization has to happen here, without blocking too much.
		
		return new WeakContract() {	@Override public void dispose() {
			_receivers.remove(weakRefFor(eventReceiver)); //Optimize consider a Set for when there is a great number of receivers.
		}};	
	}

	private void handleNewReceiver(final Consumer<? super T> receiver) {
		if (_receiverHandler == null) return;
		Environments.my(ExceptionHandler.class).shield(new Runnable() { @Override public void run() {
			_receiverHandler.consume(receiver);
		}});
	}

	private WeakRefWithAlias<Consumer<? super T>> weakRefFor(Consumer<? super T> receiver) {
		return new WeakRefWithAlias<Consumer<? super T>>(receiver);
	}

}