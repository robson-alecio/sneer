package sneer.foundation.lang;

import sneer.foundation.lang.exceptions.Refusal;


public interface PickyConsumer<T> {

	void consume(T value) throws Refusal; 

}
