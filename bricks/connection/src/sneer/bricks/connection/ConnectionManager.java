package sneer.bricks.connection;

import java.util.List;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface ConnectionManager {

	Connection openConnection(String host, int port);

	List<Connection> listConnections();

	/** 
	 * Start listening on this port. Closes the previous opened server socket
	 * @return Implement
	 */
	Consumer<Integer> sneerPortSetter();

	Signal<Integer> sneerPort(); 
}
