package sneer.pulp.connection.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threadpool.ThreadPool;

class OutgoingAttempt implements Runnable {

	@Inject
	static private Network _network;

	@Inject
	static private ConnectionManager _connectionManager;

	@Inject
	static private ThreadPool _threadPool;
	
	@Inject
	static private Clock _clock;

	private final InternetAddress _address;

	private boolean _isRunning = true;

	OutgoingAttempt(InternetAddress address) {
		_address = address;
//		_clock.wakeUpNowAndEvery(20 * 1000, this);
		_threadPool.registerActor(this);
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
		
		_connectionManager.manageOutgoingSocket(_address.contact(), socket);
	}
	
	public synchronized void crash() {
		_isRunning = false;
	}
}
