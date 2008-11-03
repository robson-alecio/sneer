package sneer.pulp.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import wheel.reactive.lists.impl.SimpleListReceiver;

class SocketOriginatorImpl implements SocketOriginator {

	@Inject
	static private InternetAddressKeeper _internetAddressKeeper;
	
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
			protected void elementToBeRemoved(InternetAddress address) {
				stopAddressing(address);
			}

			@Override
			public void elementInserted(int index, InternetAddress value) {
				throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
			}};
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
