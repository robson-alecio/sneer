package sneer.bricks.connection;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface ConnectionManager {

	Connection newConnection();

	/** 
	 * Start listening on this port. Closes the previous opened server socket
	 * @return Implement
	 */
	Consumer<Integer> sneerPortSetter();

	Signal<Integer> sneerPort(); 
}
