package sneer.bricks.pulp.reactive;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;
import sneer.foundation.commons.lang.Functor;

@Brick
public interface Signals {  

	<T> Signal<T> constant(T value);

	Consumer<Object> sink();

	<A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor);

	<A, B> Signal<B> adaptSignal(Signal<A> input, Functor<A, Signal<B>> functor);

	<T> Register<T> newRegister(T initialValue);

	<T> Reception receive(EventSource<? extends T> source, Consumer<? super T> receiver);	

	<T> Reception receive(Consumer<? super T> receiver, EventSource<? extends T>... sources);

}
