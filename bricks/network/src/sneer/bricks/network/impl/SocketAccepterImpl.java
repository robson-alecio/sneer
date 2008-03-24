package sneer.bricks.network.impl;

import sneer.bricks.network.SocketAccepter;
import wheel.io.network.ObjectSocket;
import wheel.reactive.Signal;

public class SocketAccepterImpl
    implements SocketAccepter
{

    @Override
    public Signal<ObjectSocket> lastAcceptedSocket() {
        return null;
    }

}
