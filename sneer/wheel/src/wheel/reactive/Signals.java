package wheel.reactive;

import sneer.pulp.reactive.impl.Constant;
import wheel.lang.Consumer;

public abstract class Signals {  
	
	public static <T> Constant<T> constant(T value){
		return new Constant<T>(value);
	}
	
	public static <T> Consumer<T> sink() {
		return new Consumer<T>() { @Override public void consume(T ignored) {
		}};
	}
}

