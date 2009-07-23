package sneer.bricks.network.computers.sockets.connections.originator.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;

class OutgoingAttempt {

	private final Network _network = my(Network.class);
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	private final InternetAddress _address;
	private final Contract _refToAvoidGc;

	OutgoingAttempt(InternetAddress address) {
		_address = address;

		_refToAvoidGc = my(Threads.class).keepStepping(new Steppable() { @Override public void step() {
			tryToOpen();
			my(Timer.class).sleepAtLeast(20 * 1000);
		}});

	}

	public synchronized void crash() {
		_refToAvoidGc.dispose();
	}

	private void tryToOpen() {
		ByteArraySocket socket;
		try {
			socket = _network.openSocket(_address.host(), _address.port());
		} catch (IOException e) {
			return;
		}

		_connectionManager.manageOutgoingSocket(_address.contact(), socket);
	}	
}
