package sneer.bricks.connection.impl;

import sneer.bricks.connection.SocketAccepter;
import sneer.bricks.connection.SocketReceiver;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import wheel.lang.Omnivore;

public class SocketReceiverImpl implements SocketReceiver {

	@Inject
	static private SocketAccepter _socketAccepter;
	
	@Inject
	static private ThreadPool _threadPool;

	
	private final Omnivore<ByteArraySocket> _receiverThatCannotBeGCd = new Omnivore<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
		_threadPool.registerActor(new Runnable(){@Override public void run() {
			new IndividualSocketReceiver(socket);
		}});
	}};

	SocketReceiverImpl() {
		_socketAccepter.lastAcceptedSocket().addReceiver(_receiverThatCannotBeGCd);
	}
	
	
}
