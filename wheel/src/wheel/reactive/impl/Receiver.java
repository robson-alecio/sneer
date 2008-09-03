package wheel.reactive.impl;

import java.util.ArrayList;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public abstract class Receiver<T> implements Omnivore<T> {
	
	private final List<Signal<? extends T>> _signals = new ArrayList<Signal<? extends T>>();

	public Receiver(Signal<T> signal) {
		addToSignal(signal);
	}
	
	@SuppressWarnings("unchecked")
	public Receiver(Signal... signals) {
		for (Signal<T> signal : signals) {
			addToSignal(signal);
		}
	}

	@SuppressWarnings("deprecation")
	public void addToSignal(Signal<? extends T> signal) {
		if (null == signal)
			throw new IllegalArgumentException();
		if (_signals.add(signal)) {
			signal.addReceiver(this);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeFromSignals() {
		for (Signal<? extends T> signal : _signals) {
			signal.removeReceiver(this);
		}
		_signals.clear();
	}
}
