package sneer.pulp.connection.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.threadpool.ThreadPool;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class OutgoingAttempt {

	@Inject
	static private Network _network;

	@Inject
	static private ConnectionManager _connectionManager;

	@Inject
	static private ThreadPool _threadPool;

	@Inject
	static private Clock _clock;

	private final InternetAddress _address;

	private final Object _isTryingToOpenMonitor = new Object();

	private final Receiver<Boolean> _isOnlineReceiver;
	
	private boolean _isTryingToOpen = false;

	private Signal<Boolean> _isOnline;

	OutgoingAttempt(InternetAddress address) {
		_address = address;
		_isOnline = _connectionManager.connectionFor(_address.contact()).isOnline();
		_isOnlineReceiver = new Receiver<Boolean>(_isOnline) {@Override public void consume(Boolean isOnline) {
			handleIsOnline(isOnline);
		}};
	}

	
	private void handleIsOnline(Boolean isOnline) {
		if (isOnline) return;
		
		synchronized (_isTryingToOpenMonitor) {
			if (_isTryingToOpen) return;
			_isTryingToOpen = true;
		}
		
		_threadPool.registerActor(new Runnable(){@Override public void run() {
			keepTryingToOpen();
		}});
	}

	private void keepTryingToOpen() {
		while (true) {
			
			tryToOpen();
			
			synchronized (_isTryingToOpenMonitor) {
				if (_isOnline.currentValue()) {
					_isTryingToOpen = false;
					return;
				}
			}

			_clock.sleepAtLeast(1000 * 20);
		}
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

	void crash() {
		_isOnlineReceiver.removeFromSignals();
	}
}
