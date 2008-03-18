package sneer.bricks.connection;

import java.util.List;

public interface ConnectionManager {

	Connection openConnection(String host, int port);

	List<Connection> listConnections();

	/** 
	 * Start listening on this port. Closes the previous opened server socket
	 */
	void sneerPort(int port);

	int sneerPort(); 
}
