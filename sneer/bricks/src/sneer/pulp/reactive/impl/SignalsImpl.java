package sneer.pulp.reactive.impl;

import sneer.commons.lang.Functor;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.Reception;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;

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

	@Override
	public <T> void receive(Object owner, final Consumer<? super T> delegate, EventSource<? extends T>... source) {
		ReceiversImpl.receive(owner, delegate, source);
	}

	@Override
	public <T> Reception receive(Consumer<? super T> receiver, EventSource<? extends T>... sources) {
		return ReceiversImpl.receive(receiver, sources);
	}
}
