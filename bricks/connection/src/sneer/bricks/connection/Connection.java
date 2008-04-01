package sneer.bricks.connection;

import wheel.reactive.Signal;

public interface Connection {

	Signal<Boolean> isOnline();
	
}
