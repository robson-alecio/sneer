package sneer.pulp.reactive.gates.logic.impl;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.reactive.impl.EventReceiver;
import wheel.reactive.impl.SignalOwnerReference;

class And {

	private final Register<Boolean> _result = new RegisterImpl<Boolean>(false);
	private final Signal<Boolean> _a;
	private final Signal<Boolean> _b;
	
	final Object _receiverToAvoidGc;
	
	public And(Signal<Boolean> a, Signal<Boolean> b) {
		_a = a;
		_b = b;
		_receiverToAvoidGc = new EventReceiver<Boolean>(a, b){@Override public void consume(Boolean newValueIgnored) {
			refresh();
		}};
	}
	
	private void refresh() {
		_result.setter().consume(_a.currentValue() && _b.currentValue());
	}

	public Signal<Boolean> output() {
		return new SignalOwnerReference<Boolean>(_result.output(), this);
	}
}