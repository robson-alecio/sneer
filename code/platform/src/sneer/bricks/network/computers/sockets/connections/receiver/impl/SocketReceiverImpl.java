package sneer.bricks.network.computers.sockets.connections.receiver.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.accepter.SocketAccepter;
import sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.Consumer;

class SocketReceiverImpl implements SocketReceiver {

	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private final Threads _threads = my(Threads.class);

	@SuppressWarnings("unused") private final Object _receptionRefToAvoidGc;

	SocketReceiverImpl() {
		_receptionRefToAvoidGc = my(Signals.class).receive(_socketAccepter.lastAcceptedSocket(), new Consumer<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
			_threads.startDaemon("SocketReceiverImpl", new Runnable() { @Override public void run() {
				new IndividualSocketReception(socket);
			}});
		}});
	}
}