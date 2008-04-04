package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.SocketOriginator;
import sneer.bricks.internetaddresskeeper.InternetAddress;
import sneer.bricks.internetaddresskeeper.InternetAddressKeeper;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.Startable;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class SocketOriginatorImpl implements SocketOriginator, Startable {

	@Inject
	private Injector _injector;
	
	@Inject
	private InternetAddressKeeper _internetAddressKeeper;
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<InternetAddress> _addressReceiverToAvoidGC;

	private final Map<InternetAddress, OutgoingAttempt> _attemptsByAddress = new HashMap<InternetAddress, OutgoingAttempt>();
	
	@Override
	public void start() throws Exception {
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
			}};
		
	}

	private void startAddressing(InternetAddress address) {
		OutgoingAttempt attemp = new OutgoingAttempt(_injector, address);
		_attemptsByAddress.put(address, attemp);
	}

	private void stopAddressing(InternetAddress address) {
		OutgoingAttempt attempt = _attemptsByAddress.remove(address);
		attempt.crash();
	}


}
