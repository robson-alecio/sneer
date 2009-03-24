package sneer.pulp.reactive.impl;

import sneer.commons.lang.Functor;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import wheel.lang.Consumer;

class SignalsImpl implements Signals {

	private static final Consumer<Object> SINK = new Consumer<Object>() { @Override public void consume(Object ignored){} };

	@Override
	public <T> Constant<T> constant(T value) {
		return new Constant<T>(value);
	}

	@Override
	public Consumer<Object> sink() {
		return SINK;
	}

	@Override
	public <A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor) {
		return new Adapter<A, B>(input, functor).output();
	}

}
