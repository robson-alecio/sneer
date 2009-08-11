package sneer.bricks.network.computers.sockets.connections.originator.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.foundation.lang.Consumer;

class SocketOriginatorImpl implements SocketOriginator {

	private static final Seals Seals = my(Seals.class);
	
	private final InternetAddressKeeper _internetAddressKeeper = my(InternetAddressKeeper.class);
	@SuppressWarnings("unused")
	private final Object _refToAvoidGC;
	private final Map<InternetAddress, OutgoingAttempt> _attemptsByAddress = new HashMap<InternetAddress, OutgoingAttempt>();
	
	
	SocketOriginatorImpl() {
		_refToAvoidGC = _internetAddressKeeper.addresses().addReceiver(new Consumer<CollectionChange<InternetAddress>>(){ @Override public void consume(CollectionChange<InternetAddress> value) {
			for (InternetAddress address : value.elementsRemoved()) 
				stopAddressing(address);
		
			for (InternetAddress address : value.elementsAdded()) 
				startAddressing(address);
		}});
	}

	
	private void startAddressing(InternetAddress address) {
		if (isMyOwnAddress(address)) return;
		
		OutgoingAttempt attempt = new OutgoingAttempt(address);
		_attemptsByAddress.put(address, attempt);
	}

	
	private void stopAddressing(InternetAddress address) {
		OutgoingAttempt attempt = _attemptsByAddress.remove(address);
		attempt.crash();
	}

	
	private boolean isMyOwnAddress(InternetAddress address) {
		return Seals.sealGiven(address.contact()).equals(Seals.ownSeal());
	}
	
}