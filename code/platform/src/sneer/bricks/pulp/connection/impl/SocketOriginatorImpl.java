package sneer.bricks.pulp.connection.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.pulp.connection.SocketOriginator;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.foundation.lang.Consumer;

class SocketOriginatorImpl implements SocketOriginator {

	private final InternetAddressKeeper _internetAddressKeeper = my(InternetAddressKeeper.class);
	private final Consumer<CollectionChange<InternetAddress>> _addressReceiverToAvoidGC;
	private final Map<InternetAddress, OutgoingAttempt> _attemptsByAddress = new HashMap<InternetAddress, OutgoingAttempt>();
	
	SocketOriginatorImpl() {
		_addressReceiverToAvoidGC =  new Consumer<CollectionChange<InternetAddress>>(){ @Override public void consume(CollectionChange<InternetAddress> value) {
			for (InternetAddress address : value.elementsRemoved()) 
				stopAddressing(address);

			for (InternetAddress address : value.elementsAdded()) 
				startAddressing(address);
		}};
		_internetAddressKeeper.addresses().addReceiver(_addressReceiverToAvoidGC);
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