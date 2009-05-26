package sneer.pulp.reactive.impl;

import static sneer.commons.environments.Environments.my;
import sneer.commons.lang.Functor;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;

class Adapter<IN, OUT> {

	private Register<OUT> _register = new RegisterImpl<OUT>(null);

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	Adapter(Signal<IN> input, final Functor<IN, OUT> functor) {
		_referenceToAvoidGc = ReceiversImpl.receive(new Consumer<IN>() { @Override public void consume(IN inputValue) {
			_register.setter().consume(functor.evaluate(inputValue));
		}}, input);
	}

	Signal<OUT> output() {
		return my(WeakReferenceKeeper.class).keep(_register.output(), this);
	}
}
