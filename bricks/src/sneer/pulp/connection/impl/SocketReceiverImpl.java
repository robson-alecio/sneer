package sneer.pulp.connection.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.threadpool.ThreadPool;
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
