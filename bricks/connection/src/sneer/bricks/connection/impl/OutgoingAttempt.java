package sneer.bricks.connection.impl;

import sneer.internetaddresskeeper.InternetAddress;
import sneer.lego.Crashable;
import wheel.lang.Threads;

public class OutgoingAttempt implements Crashable {

	private final InternetAddress _address;
	
	private volatile boolean _isCrashed = false;

	public OutgoingAttempt(InternetAddress address) {
		_address = address;
		
		Threads.startDaemon(new Runnable(){@Override public void run() {
			while (!_isCrashed) step();
		}});
	}

	private void step() {
		//precisa? se nao, wait num monitor.
		
		//opensocket;
		//conseguiu? se nao, pausa.
		//  se sim, avisa o cara. (o cara ignora se já tiver)
		
	}

	@Override
	public void crash() {
		_isCrashed = true;
	}

}
