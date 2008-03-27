package sneer.bricks.connection;

import sneer.bricks.network.ByteArraySocket;
import wheel.reactive.EventSource;

public interface SocketAccepter {
    
	EventSource<ByteArraySocket> lastAcceptedSocket();

}
