package sneer.bricks.connection.impl;

import java.io.IOException;

import sneer.bricks.clock.Clock;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.internetaddresskeeper.InternetAddress;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.Network;
import sneer.lego.Inject;
import sneer.lego.Crashable;
import sneer.lego.Injector;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class OutgoingAttempt implements Crashable {

	@Inject
	private Network _network;

	@Inject
	private ConnectionManager _connectionManager;

	@Inject
	Clock _clock;

	private final InternetAddress _address;

	private boolean _isTryingToOpen = false;
	private final Object _isTryingToOpenMonitor = new Object();
	
	private Signal<Boolean> _isOnline;
	private final Omnivore<Boolean> _isOnlineReceiver = new Omnivore<Boolean>() {	@Override public void consume(Boolean isOnline) {
		handleIsOnline(isOnline);
	}};


	OutgoingAttempt(Injector _injector, InternetAddress address) {
		_address = address;
		_injector.inject(this);
		_isOnline = _connectionManager.connectionFor(_address.contact()).isOnline();
		_isOnline.addReceiver(_isOnlineReceiver);
	}

	
	private void handleIsOnline(Boolean isOnline) {
		if (isOnline) return;
		
		synchronized (_isTryingToOpenMonitor) {
			if (_isTryingToOpen) return;
			_isTryingToOpen = true;
		}
		
		Threads.startDaemon(new Runnable(){@Override public void run() {
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

			_clock.sleep(1000 * 20);
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

	@Override
	public void crash() {
		_isOnline.removeReceiver(_isOnlineReceiver);
	}
}
