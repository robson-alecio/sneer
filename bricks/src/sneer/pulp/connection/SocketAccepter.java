package sneer.pulp.connection;

import sneer.pulp.network.ByteArraySocket;
import wheel.reactive.EventSource;

public interface SocketAccepter {
    
	EventSource<ByteArraySocket> lastAcceptedSocket();

}
