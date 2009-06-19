package sneer.bricks.pulp.connection;

import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.foundation.brickness.Brick;

@Brick
public interface SocketAccepter {
    
	EventSource<ByteArraySocket> lastAcceptedSocket();

}
