package wheel.reactive.impl;

import java.util.ArrayList;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.EventSource;

public abstract class Receiver<T> implements Omnivore<T> {
	
	private final List<EventSource<? extends T>> _eventSources = new ArrayList<EventSource<? extends T>>();

	public Receiver(EventSource<T> signal) {
		addToSignal(signal);
	}
	
	@SuppressWarnings("unchecked")
	public Receiver(EventSource... signals) {
		for (EventSource<T> signal : signals) {
			addToSignal(signal);
		}
	}

	@SuppressWarnings("deprecation")
	public void addToSignal(EventSource<? extends T> signal) {
		if (null == signal)
			throw new IllegalArgumentException();
		if (_eventSources.add(signal)) {
			signal.addReceiver(this);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeFromSignals() {
		for (EventSource<? extends T> signal : _eventSources) {
			signal.removeReceiver(this);
		}
		_eventSources.clear();
	}
}
