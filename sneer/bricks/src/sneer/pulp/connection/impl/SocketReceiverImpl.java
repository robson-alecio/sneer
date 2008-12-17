package sneer.pulp.connection.impl;

import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.threadpool.ThreadPool;
import wheel.reactive.impl.Receiver;
import static wheel.lang.Environments.my;

class SocketReceiverImpl implements SocketReceiver {

	private SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private ThreadPool _threadPool = my(ThreadPool.class);
	
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