package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threadpool.ThreadPool;
import sneer.software.lang.Consumer;

class SocketReceiverImpl implements SocketReceiver {

	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private final ThreadPool _threadPool = my(ThreadPool.class);
	
	SocketReceiverImpl() {
		my(Signals.class).receive(this, new Consumer<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
			_threadPool.registerActor(new Runnable(){@Override public void run() {
				new IndividualSocketReceiver(socket);
			}});
		}}, _socketAccepter.lastAcceptedSocket());
	}
}