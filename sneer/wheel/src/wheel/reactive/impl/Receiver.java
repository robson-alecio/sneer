package wheel.reactive.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.events.EventSource;

import wheel.lang.Consumer;

public abstract class Receiver<T> implements Consumer<T> {
	
	private final List<EventSource<? extends T>> _eventSources = new ArrayList<EventSource<? extends T>>();

	public Receiver(EventSource<? extends T> signal) {
		addToSignal(signal);
	}
	
	public Receiver(EventSource<? extends T>... signals) {
		for (EventSource<? extends T> signal : signals)
			addToSignal(signal);
	}

	protected Receiver() {}
	
	public void addToSignal(EventSource<? extends T> signal) {
		if (null == signal)
			throw new IllegalArgumentException();
		if (_eventSources.add(signal)) {
			signal.addReceiver(this);
		}
	}
	
	public void removeFromSignals() {
		for (EventSource<? extends T> signal : _eventSources) {
			signal.removeReceiver(this);
		}
		_eventSources.clear();
	}
}
