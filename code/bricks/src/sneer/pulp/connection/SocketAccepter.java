package sneer.pulp.connection;

import sneer.brickness.Brick;
import sneer.pulp.events.EventSource;
import sneer.pulp.network.ByteArraySocket;

@Brick
public interface SocketAccepter {
    
	EventSource<ByteArraySocket> lastAcceptedSocket();

}
