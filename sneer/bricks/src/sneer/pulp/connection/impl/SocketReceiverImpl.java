package sneer.pulp.connection.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.threadpool.ThreadPool;
import wheel.reactive.impl.Receiver;

class SocketReceiverImpl implements SocketReceiver {

	@Inject
	static private SocketAccepter _socketAccepter;
	
	@Inject
	static private ThreadPool _threadPool;
	
	@SuppressWarnings("unused")
	private final Receiver<ByteArraySocket> _receiverThatCannotBeGCd;

	SocketReceiverImpl() {
		_receiverThatCannotBeGCd = new Receiver<ByteArraySocket>(_socketAccepter.lastAcceptedSocket()) { @Override public void consume(final ByteArraySocket socket) {
			_threadPool.registerActor(new Runnable(){@Override public void run() {
				new IndividualSocketReceiver(socket);
			}});
		}};
	}
}