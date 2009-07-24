package sneer.bricks.pulp.reactive.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.ram.ref.weak.keeper.WeakReferenceKeeper;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;

class Adapter<IN, OUT> {

	private Register<OUT> _register = new RegisterImpl<OUT>(null);

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	Adapter(Signal<IN> input, final Functor<IN, OUT> functor) {
		_referenceToAvoidGc = input.addReceiver(new Consumer<IN>() { @Override public void consume(IN inputValue) {
			_register.setter().consume(functor.evaluate(inputValue));
		}});
	}

	Signal<OUT> output() {
		return my(WeakReferenceKeeper.class).keep(_register.output(), this);
	}
}
