package sneer.pulp.port.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.Consumers;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;

class PortNumberRegister {

	public PortNumberRegister(Integer initialValue) {
		_delegate = my(Signals.class).newRegister(initialValue);
	}

	private Register<Integer> _delegate;

	public PickyConsumer<Integer> setter() {
		return my(Consumers.class).newIntegerConsumerBoundaries("Sneer Port", _delegate.setter(), 0, 65535);
	}

	public Signal<Integer> output() {
		return _delegate.output();
	}

}
