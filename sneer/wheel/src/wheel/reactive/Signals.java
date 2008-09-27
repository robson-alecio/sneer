package wheel.reactive;

import wheel.reactive.impl.Constant;

public abstract class Signals {  
	
	public static <T> Signal<T> constant(T value){
		return new Constant<T>(value);
	}
}

