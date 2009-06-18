package sneer.bricks.pulp.reactive.gates.numbers.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;

class Adder {

	private final Register<Integer> _sum = my(Signals.class).newRegister(0);
	private final Signal<Integer> _a;
	private final Signal<Integer> _b;

	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	Adder(Signal<Integer> a, Signal<Integer> b) {
		_a = a;
		_b = b;

		_referenceToAvoidGc = my(Signals.class).receive(new Consumer<Integer>() {@Override public void consume(Integer newValueIgnored) {
			refresh();
		}}, a, b);
	}

	private void refresh() {
		int sum = _a.currentValue() + _b.currentValue();
		_sum.setter().consume(sum);
	}

	Signal<Integer> output() {
		return my(WeakReferenceKeeper.class).keep(_sum.output(), this);
	}

}
