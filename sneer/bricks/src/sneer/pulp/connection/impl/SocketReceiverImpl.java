package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

class SocketReceiverImpl implements SocketReceiver {

	private final SocketAccepter _socketAccepter = my(SocketAccepter.class);
	
	private final Threads _threads = my(Threads.class);

	@SuppressWarnings("unused") private final Object _receptionRefToAvoidGc;

	private Stepper _stepperRefToAvoidGc;

	SocketReceiverImpl() {
		_receptionRefToAvoidGc = my(Signals.class).receive(_socketAccepter.lastAcceptedSocket(), new Consumer<ByteArraySocket>() { @Override public void consume(final ByteArraySocket socket) {
			_stepperRefToAvoidGc = new Stepper() { @Override public boolean step() {
				new IndividualSocketReceiver(socket);
				return false;
			}};

			_threads.registerStepper(_stepperRefToAvoidGc);
		}});
	}
}