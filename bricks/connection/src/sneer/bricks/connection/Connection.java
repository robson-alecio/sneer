package sneer.bricks.connection;

import wheel.reactive.Signal;

public interface Connection {

	Signal<Boolean> isConnected();

	void close();
	
	void waitUntilConnected();

	void connect(String host, int port);
	
	void write(Object packet) throws Exception;
	
	Object read() throws Exception;
	

}