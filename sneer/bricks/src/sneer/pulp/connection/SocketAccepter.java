package sneer.pulp.connection;

import sneer.pulp.events.EventSource;
import sneer.pulp.network.ByteArraySocket;

public interface SocketAccepter {
    
	EventSource<ByteArraySocket> lastAcceptedSocket();

}
