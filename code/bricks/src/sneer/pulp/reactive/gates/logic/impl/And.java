package sneer.pulp.reactive.gates.logic.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;

class And {

	private final Register<Boolean> _result = my(Signals.class).newRegister(false);
	private final Signal<Boolean> _a;
	private final Signal<Boolean> _b;

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	public And(Signal<Boolean> a, Signal<Boolean> b) {
		_a = a;
		_b = b;

		_referenceToAvoidGc = my(Signals.class).receive(new Consumer<Boolean>(){@Override public void consume(Boolean newValueIgnored) {
			refresh();
		}}, a, b);
	}

	private void refresh() {
		_result.setter().consume(_a.currentValue() && _b.currentValue());
	}

	public Signal<Boolean> output() {
		return my(WeakReferenceKeeper.class).keep(_result.output(), this);
	}
}