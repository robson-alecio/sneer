package sneer.bricks.connection.impl;

import sneer.bricks.connection.ConnectionReceiver;
import sneer.bricks.connection.SocketAccepter;
import sneer.bricks.network.ByteArraySocket;
import sneer.lego.Brick;
import sneer.lego.Startable;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class ConnectionReceiverImpl implements ConnectionReceiver, Startable {

	@Brick
	private SocketAccepter _socketAccepter;

	@Override
	public void start() throws Exception {
		_socketAccepter.lastAcceptedSocket().addReceiver(new Omnivore<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
			Threads.startDaemon(new Runnable(){@Override public void run() {
				new IndividualConnectionReceiver(socket);
			}});
		}});
	}

}
