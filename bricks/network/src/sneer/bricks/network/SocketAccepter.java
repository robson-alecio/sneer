package sneer.bricks.network;

import wheel.reactive.EventSource;

public interface SocketAccepter {
	
    EventSource<ByteArraySocket> lastAcceptedSocket();
    
}
