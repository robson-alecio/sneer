package sneer.pulp.reactive.gates.numbers.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import wheel.reactive.impl.SignalOwnerReference;

class Adder {

	private final Register<Integer> _sum = my(Signals.class).newRegister(0);
	private final Signal<Integer> _a;
	private final Signal<Integer> _b;

	Adder(Signal<Integer> a, Signal<Integer> b) {
		_a = a;
		_b = b;

		my(Signals.class).receive(output(), new Consumer<Integer>() {@Override public void consume(Integer newValueIgnored) {
			refresh();
		}}, a, b);
	}

	private void refresh() {
		int sum = _a.currentValue() + _b.currentValue();
		_sum.setter().consume(sum);
	}

	Signal<Integer> output() {
		return new SignalOwnerReference<Integer>(_sum.output(), this);
	}

}
