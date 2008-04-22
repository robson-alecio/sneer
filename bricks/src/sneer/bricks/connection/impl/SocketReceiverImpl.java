package sneer.bricks.connection.impl;

import sneer.bricks.connection.SocketAccepter;
import sneer.bricks.connection.SocketReceiver;
import sneer.bricks.network.ByteArraySocket;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.Startable;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class SocketReceiverImpl implements SocketReceiver, Startable {

	@Inject
	private SocketAccepter _socketAccepter;
	
	@Inject
	private Injector _injector;

	private final Omnivore<ByteArraySocket> _receiverThatCannotBeGCd = new Omnivore<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
		Threads.startDaemon(new Runnable(){@Override public void run() {
			new IndividualSocketReceiver(_injector, socket);
		}});
	}};

	
	@Override
	public void start() throws Exception {
		_socketAccepter.lastAcceptedSocket().addReceiver(_receiverThatCannotBeGCd);
	}

}
