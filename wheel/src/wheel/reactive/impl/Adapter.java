package wheel.reactive.impl;

import wheel.lang.Functor;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;

public class Adapter<T1, T2> {

	private Omnivore<T1> _receiver;
	private Register<T2> _register = new RegisterImpl<T2>(null);

	public Adapter(Signal<T1> input, final Functor<T1, T2> functor) {
		_receiver = new Omnivore<T1>() { @Override public void consume(T1 inputValue) {
			_register.setter().consume(functor.evaluate(inputValue));
		}};
		input.addReceiver(_receiver);
	}

	public Signal<T2> output() {
		return new SignalOwnerReference<T2>(_register.output(), this);
	}

}
