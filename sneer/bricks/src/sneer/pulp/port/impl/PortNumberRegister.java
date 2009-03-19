package sneer.pulp.port.impl;

import sneer.pulp.reactive.Signal;
import wheel.lang.PickyConsumer;
import wheel.reactive.impl.IntegerConsumerBoundaries;
import wheel.reactive.impl.RegisterImpl;

class PortNumberRegister {

	public PortNumberRegister(Integer initialValue) {
		_delegate = new RegisterImpl<Integer>(initialValue);
	}

	private RegisterImpl<Integer> _delegate;

	public PickyConsumer<Integer> setter() {
		return new IntegerConsumerBoundaries("Sneer Port", _delegate.setter(), 0, 65535);
	}

	public Signal<Integer> output() {
		return _delegate.output();
	}

}
