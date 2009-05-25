package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.Threads;

class SocketReceiverImpl implements SocketReceiver {

	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private final Threads _threads = my(Threads.class);

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	SocketReceiverImpl() {
		_referenceToAvoidGc = my(Signals.class).receive(new Consumer<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
			_threads.registerActor(new Runnable(){@Override public void run() {
				new IndividualSocketReceiver(socket);
			}});
		}}, _socketAccepter.lastAcceptedSocket());
	}
}