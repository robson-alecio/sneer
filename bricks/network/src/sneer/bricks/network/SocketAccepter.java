package sneer.bricks.network;

import wheel.io.network.ObjectSocket;
import wheel.reactive.EventSource;

public interface SocketAccepter {
    EventSource<ObjectSocket> lastAcceptedSocket();
}
