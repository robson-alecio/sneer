package wheel.reactive;

import sneer.pulp.events.EventSource;
import wheel.lang.Consumer;
import wheel.reactive.impl.Receiver;

public class Solder<T> extends Receiver<T> {

	private final Consumer<? super T> _omnivore;

	public Solder(EventSource<? extends T> signal, Consumer<? super T> omnivore) {
		_omnivore = omnivore;
		addToSignal(signal);
	}

	@Override
	public void consume(T value) {
		_omnivore.consume(value);
	}

}
