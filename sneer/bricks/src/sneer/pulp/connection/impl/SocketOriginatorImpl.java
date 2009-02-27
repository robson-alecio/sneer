package sneer.pulp.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import wheel.reactive.lists.impl.SimpleListReceiver;
import static sneer.brickness.Environments.my;

class SocketOriginatorImpl implements SocketOriginator {

	private final InternetAddressKeeper _internetAddressKeeper = my(InternetAddressKeeper.class);
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<InternetAddress> _addressReceiverToAvoidGC;

	private final Map<InternetAddress, OutgoingAttempt> _attemptsByAddress = new HashMap<InternetAddress, OutgoingAttempt>();
	
	SocketOriginatorImpl() {
		_addressReceiverToAvoidGC = new SimpleListReceiver<InternetAddress>(_internetAddressKeeper.addresses()) {
			
			@Override
			protected void elementAdded(InternetAddress address) {
				startAddressing(address);
			}
			
			@Override
			protected void elementPresent(InternetAddress address) {
				startAddressing(address);
			}
			
			@Override
			protected void elementRemoved(InternetAddress address) {
				stopAddressing(address);
			}
		};
	}
	

	private void startAddressing(InternetAddress address) {
		OutgoingAttempt attempt = new OutgoingAttempt(address);
		_attemptsByAddress.put(address, attempt);
	}

	private void stopAddressing(InternetAddress address) {
		OutgoingAttempt attempt = _attemptsByAddress.remove(address);
		attempt.crash();
	}


}
