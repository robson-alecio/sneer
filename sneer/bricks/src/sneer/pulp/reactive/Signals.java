package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.commons.lang.Functor;
import sneer.pulp.reactive.impl.Constant;
import wheel.lang.Consumer;

public interface Signals extends Brick {  
	
	<T> Constant<T> constant(T value);
	
	Consumer<Object> sink();
	
	<A, B> Signal<B> adapt(Signal<A> input, Functor<A, B> functor);
	
}

