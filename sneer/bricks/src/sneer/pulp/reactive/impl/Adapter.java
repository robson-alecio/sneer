package sneer.pulp.reactive.impl;

import static sneer.commons.environments.Environments.my;
import sneer.commons.lang.Functor;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import wheel.reactive.impl.EventReceiver;

class Adapter<IN, OUT> {

	@SuppressWarnings("unused")
	private EventReceiver<IN> _receiver;
	
	private Register<OUT> _register = new RegisterImpl<OUT>(null);

	Adapter(Signal<IN> input, final Functor<IN, OUT> functor) {
		_receiver = new EventReceiver<IN>(input) { @Override public void consume(IN inputValue) {
			_register.setter().consume(functor.evaluate(inputValue));
		}};
	}

	Signal<OUT> output() {
		return my(WeakReferenceKeeper.class).keep(_register.output(), this);
	}
}
