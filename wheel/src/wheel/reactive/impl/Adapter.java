package wheel.reactive.impl;

import wheel.lang.Functor;
import wheel.reactive.Register;
import wheel.reactive.Signal;

public class Adapter<T1, T2> {

	@SuppressWarnings("unused")
	private Receiver<T1> _receiver;
	
	private Register<T2> _register = new RegisterImpl<T2>(null);

	public Adapter(Signal<T1> input, final Functor<T1, T2> functor) {
		_receiver = new Receiver<T1>(input) { @Override public void consume(T1 inputValue) {
			_register.setter().consume(functor.evaluate(inputValue));
		}};
	}

	public Signal<T2> output() {
		return _register.output();
	}
}
