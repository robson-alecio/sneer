package sneer.kernel.communication;

import wheel.reactive.Signal;

public interface Connection {

	Signal<Boolean> isOnline();

}
