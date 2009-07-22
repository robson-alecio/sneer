package sneer.bricks.network.computers.sockets.connections.originator.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;

class OutgoingAttempt {

	private final Network _network = my(Network.class);
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	private final Clock _clock = my(Clock.class);
	private final InternetAddress _address;
	private final Steppable _refToAvoidGc;

	private boolean _isRunning = true;

	OutgoingAttempt(InternetAddress address) {
		_address = address;

		_refToAvoidGc = new Steppable() { @Override public boolean step() {
			if (isRunning()) {
				tryToOpen();
				_clock.sleepAtLeast(20 * 1000);
				return true;
			}

			return false;
		}};

		my(Threads.class).newStepper(_refToAvoidGc);
	}

	public synchronized void crash() {
		_isRunning = false;
	}

	private synchronized boolean isRunning() {
		return _isRunning;
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
