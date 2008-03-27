package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.internetaddresskeeper.InternetAddress;

class OutgoingSocketPool {

	private final Map<InternetAddress, OutgoingAttempt> _attemptsByAddress = new HashMap<InternetAddress, OutgoingAttempt>();
	
	void startAddressing(InternetAddress address) {
		_attemptsByAddress.put(address, new OutgoingAttempt(address));
	}

	void stopAddressing(InternetAddress address) {
		OutgoingAttempt attempt = _attemptsByAddress.remove(address);
		attempt.crash();
	}

	boolean isEmpty() {
		return _attemptsByAddress.isEmpty();
	}

}
