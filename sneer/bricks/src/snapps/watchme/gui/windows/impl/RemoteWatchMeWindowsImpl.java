package snapps.watchme.gui.windows.impl;

import java.util.HashMap;
import java.util.Map;

import snapps.watchme.gui.windows.RemoteWatchMeWindows;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import wheel.reactive.lists.impl.SimpleListReceiver;
import static wheel.lang.Environments.my;

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