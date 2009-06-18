package sneer.foundation.lang;

import sneer.foundation.lang.exceptions.Refusal;


public interface PickyConsumer<T> {

	void consume(T value) throws Refusal; 	// Fix For now, throwing of the exception has been removed because of a bug in the Eclipse compiler . Refactor: See wheel.lang.exceptions.IllegalParameter and throw the Exception again. 

}
