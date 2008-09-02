package wheel.reactive.impl;

import wheel.lang.Omnivore;
import wheel.lang.Types;
import wheel.reactive.Signal;

public abstract class Receiver<T> implements Omnivore<T> {
	
	private final Object _signalReference;

	public Receiver(Signal<T> signal) {
		signal.addReceiver(this);
		_signalReference = signal;
	}
	
	@SuppressWarnings("unchecked")
	public Receiver(Signal... signals) {
		for (Signal<T> signal : signals) {
			signal.addReceiver(this);
		}
		_signalReference = signals;
	}
	
	public void removeFromSignals() {
		for (Signal<T> signal : signals()) {
			signal.removeReceiver(this);
		}
	}

	private Signal<T>[] signals() {
		if (_signalReference instanceof Signal[]) {
			return Types.cast(_signalReference);
		}
		return Types.cast(new Signal[] { Types.cast(_signalReference) });
	}

}
