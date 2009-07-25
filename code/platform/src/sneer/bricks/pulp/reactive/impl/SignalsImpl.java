package sneer.bricks.pulp.reactive.impl;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;

class SignalsImpl implements Signals {

	private static final Consumer<Object> SINK = new Consumer<Object>() { @Override public void consume(Object ignored){} };

	@Override
	public <T> Signal<T> constant(T value) {
		return new ConstantImpl<T>(value);
	}

	@Override
	public Consumer<Object> sink() {
		return SINK;
	}

	@Override
	public <A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor) {
		return new Adapter<A, B>(input, functor).output();
	}

	@Override
	public <A, B> Signal<B> adaptSignal(Signal<A> input, Functor<A, Signal<B>> functor) {
		return new SignalAdapter<A, B>(input, functor).output();
	}

	@Override
	public <T> Register<T> newRegister(T initialValue) {
		return new RegisterImpl<T>(initialValue);
	}

}
