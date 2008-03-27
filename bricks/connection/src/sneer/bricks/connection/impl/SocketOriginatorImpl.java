package sneer.bricks.connection.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.connection.SocketOriginator;
import sneer.contacts.Contact;
import sneer.internetaddresskeeper.InternetAddress;
import sneer.internetaddresskeeper.InternetAddressKeeper;
import sneer.lego.Brick;
import sneer.lego.Startable;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class SocketOriginatorImpl implements SocketOriginator, Startable {

	@Brick
	private InternetAddressKeeper _internetAddressKeeper;
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<InternetAddress> _addressReceiverToAvoidGC;

	private final Map<Contact, OutgoingSocketPool> _socketPoolByContact = new HashMap<Contact, OutgoingSocketPool>();

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
		Contact contact = address.contact();
		OutgoingSocketPool pool = produceSocketPoolFor(contact);
		pool.startAddressing(address);
	}

	private OutgoingSocketPool produceSocketPoolFor(Contact contact) {
		OutgoingSocketPool pool = _socketPoolByContact.get(contact);
		if (pool == null) {
			pool = new OutgoingSocketPool();
			_socketPoolByContact.put(contact, pool);
		}
		return pool;
	}

	private void stopAddressing(InternetAddress address) {
		Contact contact = address.contact();
		OutgoingSocketPool pool = produceSocketPoolFor(contact);
		pool.stopAddressing(address);
		if (pool.isEmpty())
			_socketPoolByContact.remove(contact);
	}


}
