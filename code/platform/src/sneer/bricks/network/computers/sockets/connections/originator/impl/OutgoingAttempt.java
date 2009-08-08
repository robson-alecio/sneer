package sneer.bricks.network.computers.sockets.connections.originator.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;

class OutgoingAttempt {

	private final Network _network = my(Network.class);
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	private final InternetAddress _address;
	private final WeakContract _steppingContract;

	private AtomicBoolean _isOpening = new AtomicBoolean(false);
	
	OutgoingAttempt(InternetAddress address) {
		_address = address;

		_steppingContract = my(Timer.class).wakeUpNowAndEvery(20 * 1000, new Steppable() { @Override public void step() {
			my(Threads.class).startDaemon("Outgoing Attempt", new Runnable() { @Override public void run() {
				tryToOpenOnlyOnce();
			}});
		}});

	}

	private void tryToOpenOnlyOnce() {
		if (_isOpening.getAndSet(true)) return;
		try {
			tryToOpen();
		} finally {
			_isOpening.set(false);
		}
	}	

	private void tryToOpen() {
		my(Logger.class).log("Trying to open socket to: {} port: {}", _address.host(), _address.port());

		ByteArraySocket socket;
		try {
			socket = _network.openSocket(_address.host(), _address.port());
		} catch (IOException e) {
			return;
		}

		my(Logger.class).log("Socket opened to: {} port: {}", _address.host(), _address.port());
		_connectionManager.manageOutgoingSocket(_address.contact(), socket);
	}

	public synchronized void crash() {
		_steppingContract.dispose();
	}
	
}
