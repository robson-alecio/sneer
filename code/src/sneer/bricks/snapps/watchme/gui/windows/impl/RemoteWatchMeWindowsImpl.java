package sneer.bricks.snapps.watchme.gui.windows.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.contacts.ContactManager;
import sneer.bricks.pulp.reactive.collections.impl.SimpleListReceiver;
import sneer.bricks.snapps.watchme.gui.windows.RemoteWatchMeWindows;

class RemoteWatchMeWindowsImpl implements RemoteWatchMeWindows{

	Map<Contact, WatchMeReceiver> _remoteReceivers = new HashMap<Contact, WatchMeReceiver>();

	private final ContactManager _contactManager = my(ContactManager.class);

	@SuppressWarnings("unused")
	private WatchMeReceiver _ownReceiverToAvoidGc;

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactReceiverToAvoidGc = new SimpleListReceiver<Contact>(_contactManager.contacts()){

		@Override
		protected void elementAdded(Contact newElement) {
			startReceivingScreensFrom(newElement);			
		}

		@Override
		protected void elementPresent(Contact element) {
			startReceivingScreensFrom(element);			
		}

		@Override
		protected void elementRemoved(Contact contact) {
			stopReceivingScreensFrom(contact);
		}

	};
	
	public void startReceivingScreensFrom(Contact contact) {
		WatchMeReceiver receiver = new WatchMeReceiver(contact);
		_remoteReceivers.put(contact, receiver);
	}

	public void stopReceivingScreensFrom(Contact contact) {
		WatchMeReceiver receiver = _remoteReceivers.get(contact);
		_remoteReceivers.remove(contact);
		receiver.dispose();
	}
}