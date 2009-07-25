package sneer.bricks.pulp.reactive;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;

@Brick
public interface Signals {  

	<T> Register<T> newRegister(T initialValue);
	<T> Signal<T> constant(T value);

	Consumer<Object> sink();

	<A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor);
	<A, B> Signal<B> adaptSignal(Signal<A> input, Functor<A, Signal<B>> functor);

}
