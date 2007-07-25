package wheel.io.network;

import wheel.lang.Consumer;
import wheel.lang.IntegerConsumerBoundaries;
import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;

public class PortNumberSource {

	public PortNumberSource(Integer initialValue) {
		_delegate = new SourceImpl<Integer>(initialValue);
	}

	private SourceImpl<Integer> _delegate;

	public Consumer<Integer> setter() {
		return new IntegerConsumerBoundaries("Sneer Port", _delegate.setter(), 0, 65535);
	}

	public Signal<Integer> output() {
		return _delegate.output();
	}


	private static final long serialVersionUID = 1L;
}
