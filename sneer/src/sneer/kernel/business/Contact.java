package sneer.kernel.business;

import wheel.reactive.Signal;

public interface Contact {

	Signal<Boolean> isOnline();
	
}
