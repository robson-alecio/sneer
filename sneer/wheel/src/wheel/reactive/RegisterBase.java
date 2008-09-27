package wheel.reactive;

import java.io.Serializable;

public interface RegisterBase extends Serializable {

	CollectionSignal<?> output();
	
}
