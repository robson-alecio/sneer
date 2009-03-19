package wheel.io.network;

import java.io.Serializable;

import sneer.pulp.reactive.Signal;

import wheel.lang.PickyConsumer;
import wheel.reactive.impl.IntegerConsumerBoundaries;
import wheel.reactive.impl.RegisterImpl;

public class PortNumberRegister implements Serializable{

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


	private static final long serialVersionUID = 1L;
}
