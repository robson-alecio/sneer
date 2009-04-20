package sneer.pulp.reactive.gates.logic.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import wheel.reactive.impl.SignalOwnerReference;

class And {

	private final Register<Boolean> _result = my(Signals.class).newRegister(false);
	private final Signal<Boolean> _a;
	private final Signal<Boolean> _b;

	public And(Signal<Boolean> a, Signal<Boolean> b) {
		_a = a;
		_b = b;

		my(Signals.class).receive(output(), new Consumer<Boolean>(){@Override public void consume(Boolean newValueIgnored) {
			refresh();
		}}, a, b);
	}

	private void refresh() {
		_result.setter().consume(_a.currentValue() && _b.currentValue());
	}

	public Signal<Boolean> output() {
		return new SignalOwnerReference<Boolean>(_result.output(), this);
	}
}