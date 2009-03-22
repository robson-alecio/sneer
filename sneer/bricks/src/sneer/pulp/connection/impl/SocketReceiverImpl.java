package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.threadpool.ThreadPool;
import wheel.reactive.impl.EventReceiver;

class SocketReceiverImpl implements SocketReceiver {

	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private final ThreadPool _threadPool = my(ThreadPool.class);
	
	@SuppressWarnings("unused")
	private final EventReceiver<ByteArraySocket> _receiverThatCannotBeGCd;

	SocketReceiverImpl() {
		_receiverThatCannotBeGCd = new EventReceiver<ByteArraySocket>(_socketAccepter.lastAcceptedSocket()) { @Override public void consume(final ByteArraySocket socket) {
			_threadPool.registerActor(new Runnable(){@Override public void run() {
				new IndividualSocketReceiver(socket);
			}});
		}};
	}
}