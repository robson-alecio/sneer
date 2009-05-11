package sneer.pulp.port.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.hardware.cpu.utils.consumers.validators.bounds.integer.IntegerBoundsFactory;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;

class PortNumberRegister {

	public PortNumberRegister(Integer initialValue) {
		_delegate = my(Signals.class).newRegister(initialValue);
	}

	private Register<Integer> _delegate;

	public PickyConsumer<Integer> setter() {
		return my(IntegerBoundsFactory.class).newIntegerBounds("Sneer Port", _delegate.setter(), 0, 65535);
	}

	public Signal<Integer> output() {
		return _delegate.output();
	}

}
