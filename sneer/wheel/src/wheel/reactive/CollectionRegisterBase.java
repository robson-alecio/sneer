package wheel.reactive;

import java.io.Serializable;

import sneer.pulp.reactive.CollectionSignal;

public interface CollectionRegisterBase extends Serializable {

	CollectionSignal<?> output();
	
}
