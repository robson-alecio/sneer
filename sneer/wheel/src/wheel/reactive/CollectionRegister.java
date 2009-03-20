package wheel.reactive;

import sneer.pulp.reactive.CollectionSignal;

public interface CollectionRegister<T> {

	CollectionSignal<T> output();
	
}
