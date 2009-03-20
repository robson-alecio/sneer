package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.pulp.reactive.impl.Constant;
import wheel.lang.Consumer;

public interface Signals extends Brick {  
	
	<T> Constant<T> constant(T value);
	Consumer<Object> sink();
	
}

