package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.log.Logger;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threads.Threads;

class OutgoingAttempt {

	private final Network _network = my(Network.class);
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	private final Clock _clock = my(Clock.class);
	
	private final InternetAddress _address;
	private boolean _isRunning = true;

	
	OutgoingAttempt(InternetAddress address) {
		_address = address;

		my(Threads.class).registerActor(new Runnable(){ @Override public void run() {
			keepTryingToOpen();
		}});
	}

	
	public synchronized void crash() {
		_isRunning = false;
	}

	
	private void keepTryingToOpen() {
		while (isRunning()) {
			tryToOpen();
			_clock.sleepAtLeast(20 * 1000);
		}
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
		
		my(Logger.class).log("Outgoing socket opened: " + socket);
		_connectionManager.manageOutgoingSocket(_address.contact(), socket);
	}
	
}
