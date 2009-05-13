package sneer.pulp.reactive.impl;

import static sneer.commons.environments.Environments.my;
import sneer.commons.lang.Functor;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import wheel.reactive.impl.EventReceiver;

class SignalAdapter<IN, OUT> {

	@SuppressWarnings("unused")	private final Object _refToAvoidGC;
	@SuppressWarnings("unused")	private Object _refToAvoidGC2;
	
	private Register<OUT> _register = new RegisterImpl<OUT>(null);

	SignalAdapter(Signal<IN> input, final Functor<IN, Signal<OUT>> functor) {
		_refToAvoidGC = new EventReceiver<IN>(input) { @Override public void consume(IN inputValue) {
			Signal<OUT> newSignal = functor.evaluate(inputValue);
			_refToAvoidGC2 = new EventReceiver<OUT>(newSignal) { @Override public void consume(OUT value) {
				_register.setter().consume(value);
			}};
		}};
	}

	Signal<OUT> output() {
		return my(WeakReferenceKeeper.class).keep(_register.output(), this);
	}
}
