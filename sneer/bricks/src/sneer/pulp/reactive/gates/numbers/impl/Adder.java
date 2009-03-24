package sneer.pulp.reactive.gates.numbers.impl;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.reactive.impl.EventReceiver;
import wheel.reactive.impl.SignalOwnerReference;

class Adder {

	private final Register<Integer> _sum = new RegisterImpl<Integer>(0);
	private final Signal<Integer> _a;
	private final Signal<Integer> _b;
	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;
	
	Adder(Signal<Integer> a, Signal<Integer> b) {
		_a = a;
		_b = b;
		_referenceToAvoidGc = new EventReceiver<Integer>(a, b){@Override public void consume(Integer newValueIgnored) {
			refresh();
		}};
	}
	
	private void refresh() {
		int sum = _a.currentValue() + _b.currentValue();
		_sum.setter().consume(sum);
	}

	Signal<Integer> output() {
		return new SignalOwnerReference<Integer>(_sum.output(), this);
	}

}
