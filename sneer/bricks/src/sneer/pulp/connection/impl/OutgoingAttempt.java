package sneer.pulp.connection.impl;

import java.io.IOException;

import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threadpool.ThreadPool;
import wheel.io.Logger;
import static sneer.brickness.Environments.my;

class OutgoingAttempt implements Runnable {

	private final Network _network = my(Network.class);
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	private final ThreadPool _threadPool = my(ThreadPool.class); { _threadPool.registerActor(this); }
	private final Clock _clock = my(Clock.class);

	
	private final InternetAddress _address;
	private boolean _isRunning = true;

	
	OutgoingAttempt(InternetAddress address) {
		_address = address;
	}

	
	public synchronized void crash() {
		_isRunning = false;
	}

	
	public void run() {
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
		
		Logger.log("Outgoing socket opened: " + socket);
		_connectionManager.manageOutgoingSocket(_address.contact(), socket);
	}
	
}
