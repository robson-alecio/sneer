package wheel.reactive;

import sneer.pulp.events.EventSource;
import wheel.lang.Consumer;
import wheel.reactive.impl.Receiver;

public class Solder<T> extends Receiver<T> {

	private final Consumer<? super T> _receiver;

	public Solder(EventSource<? extends T> signal, Consumer<? super T> receiver) {
		_receiver = receiver;
		addToSignal(signal);
	}

	@Override
	public void consume(T value) {
		_receiver.consume(value);
	}

}
