package sneer.bricks.pulp.reactive.gates.logic.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.ram.ref.weak.keeper.WeakReferenceKeeper;
import sneer.bricks.pulp.events.pulsers.Pulsers;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;

class And {

	private final Register<Boolean> _result = my(Signals.class).newRegister(false);
	private final Signal<Boolean> _a;
	private final Signal<Boolean> _b;

	@SuppressWarnings("unused") private final WeakContract _referenceToAvoidGc;

	public And(Signal<Boolean> a, Signal<Boolean> b) {
		_a = a;
		_b = b;

		_referenceToAvoidGc = my(Pulsers.class).receive(new Runnable(){@Override public void run() {
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