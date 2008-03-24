package sneer.bricks.network;

import wheel.io.network.ObjectSocket;
import wheel.reactive.Signal;

public interface SocketAccepter {
    Signal<ObjectSocket> lastAcceptedSocket();
}
