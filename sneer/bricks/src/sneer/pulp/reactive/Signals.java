package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.commons.lang.Functor;
import wheel.lang.Consumer;

@Brick
public interface Signals {  
	
	<T> Signal<T> constant(T value);
	
	Consumer<Object> sink();
	
	<A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor);
	<A, B> Signal<B> adaptSignal(Signal<A> input, Functor<A, Signal<B>> functor);
	
}

