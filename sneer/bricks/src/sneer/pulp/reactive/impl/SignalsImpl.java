package sneer.pulp.reactive.impl;

import sneer.pulp.reactive.Signals;
import wheel.lang.Consumer;

public class SignalsImpl implements Signals {

	private static final Consumer<Object> SINK = new Consumer<Object>() { @Override public void consume(Object ignored){} };

	@Override
	public <T> Constant<T> constant(T value) {
		return new Constant<T>(value);
	}

	@Override
	public Consumer<Object> sink() {
		return SINK;
	}

}
