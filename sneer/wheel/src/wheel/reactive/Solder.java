package wheel.reactive;

import wheel.lang.Omnivore;
import wheel.reactive.impl.Receiver;

public class Solder<T> extends Receiver<T> {

	private final Omnivore<T> _omnivore;

	public Solder(Signal<T> signal, Omnivore<T> omnivore) {
		_omnivore = omnivore;
		addToSignal(signal);
	}

	@Override
	public void consume(T value) {
		_omnivore.consume(value);
	}

}
