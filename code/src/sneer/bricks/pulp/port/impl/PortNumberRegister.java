package sneer.bricks.pulp.port.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.utils.consumers.validators.bounds.integer.IntegerBounds;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.PickyConsumer;

class PortNumberRegister {

	public PortNumberRegister(Integer initialValue) {
		_delegate = my(Signals.class).newRegister(initialValue);
	}

	private Register<Integer> _delegate;

	public PickyConsumer<Integer> setter() {
		return my(IntegerBounds.class).newInstance("Sneer Port", _delegate.setter(), 0, 65535);
	}

	public Signal<Integer> output() {
		return _delegate.output();
	}

}
