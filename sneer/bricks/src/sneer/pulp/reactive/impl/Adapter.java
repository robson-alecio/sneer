package sneer.pulp.reactive.impl;

import sneer.commons.lang.Functor;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import wheel.reactive.impl.EventReceiver;
import wheel.reactive.impl.SignalOwnerReference;

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
		return new SignalOwnerReference<OUT>(_register.output(), this);
	}
}
