package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.SocketOriginator;
import sneer.internetaddresskeeper.InternetAddress;
import sneer.internetaddresskeeper.InternetAddressKeeper;
import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.Startable;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class SocketOriginatorImpl implements SocketOriginator, Startable {

	@Brick
	private Container _container;
	
	@Brick
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
		_attemptsByAddress.put(address, _container.create(OutgoingAttempt.class,address));
	}

	private void stopAddressing(InternetAddress address) {
		OutgoingAttempt attempt = _attemptsByAddress.remove(address);
		attempt.crash();
	}


}
