package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

class OutgoingAttempt {

	private final Network _network = my(Network.class);
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	private final Clock _clock = my(Clock.class);
	private final InternetAddress _address;
	private final Stepper _refToAvoidGc;

	private boolean _isRunning = true;

	OutgoingAttempt(InternetAddress address) {
		_address = address;

		_refToAvoidGc = new Stepper() { @Override public boolean step() {
			if (isRunning()) {
				tryToOpen();
				_clock.sleepAtLeast(20 * 1000);
				return true;
			}

			return false;
		}};

		my(Threads.class).registerStepper(_refToAvoidGc);
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
