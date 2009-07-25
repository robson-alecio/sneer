package sneer.bricks.pulp.reactive.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.ram.ref.weak.keeper.WeakReferenceKeeper;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;

class SignalAdapter<IN, OUT> {

	@SuppressWarnings("unused")	private final Object _refToAvoidGC;
	private WeakContract _refToAvoidGC2;

	private Register<OUT> _register = new RegisterImpl<OUT>(null);

	SignalAdapter(Signal<IN> input, final Functor<IN, Signal<OUT>> functor) {
		_refToAvoidGC = input.addReceiver(new Consumer<IN>() { @Override public void consume(IN inputValue) {
			Signal<OUT> newSignal = functor.evaluate(inputValue);
			if (_refToAvoidGC2 != null) _refToAvoidGC2.dispose();
			_refToAvoidGC2 = newSignal.addReceiver(new Consumer<OUT>() { @Override public void consume(OUT value) {
				_register.setter().consume(value);
			}});
		}});
	}

	Signal<OUT> output() {
		return my(WeakReferenceKeeper.class).keep(_register.output(), this);
	}
}
