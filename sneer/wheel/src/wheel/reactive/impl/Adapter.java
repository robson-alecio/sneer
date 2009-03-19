package wheel.reactive.impl;

import sneer.commons.lang.Functor;
import sneer.pulp.reactive.Signal;
import wheel.reactive.Register;

public class Adapter<IN, OUT> {

	@SuppressWarnings("unused")
	private Receiver<IN> _receiver;
	
	private Register<OUT> _register = new RegisterImpl<OUT>(null);

	public Adapter(Signal<IN> input, final Functor<IN, OUT> functor) {
		_receiver = new Receiver<IN>(input) { @Override public void consume(IN inputValue) {
			_register.setter().consume(functor.evaluate(inputValue));
		}};
	}

	public Signal<OUT> output() {
		return new SignalOwnerReference<OUT>(_register.output(), this);
	}
}
