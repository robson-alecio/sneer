package wheel.reactive;

import java.io.Serializable;

public interface CollectionRegisterBase extends Serializable {

	CollectionSignal<?> output();
	
}
