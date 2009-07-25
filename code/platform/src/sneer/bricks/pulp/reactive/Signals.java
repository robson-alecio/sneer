package sneer.bricks.pulp.reactive;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;

@Brick
public interface Signals {  

	<T> Register<T> newRegister(T initialValue);
	<T> Signal<T> constant(T value);

	<T> WeakContract receive(EventSource<? extends T> source, Consumer<? super T> receiver);	

	Consumer<Object> sink();

	<A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor);
	<A, B> Signal<B> adaptSignal(Signal<A> input, Functor<A, Signal<B>> functor);

}
