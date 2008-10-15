package snapps.watchme.gui.impl;

import java.util.HashMap;
import java.util.Map;

import snapps.watchme.gui.RemoteWatchMeWindows;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class RemoteWatchMeWindowsImpl implements RemoteWatchMeWindows{

	@Inject
	static private ContactManager _contactManager;
	
	Map<Contact, WatchMeReceiver> _remoteReceivers = new HashMap<Contact, WatchMeReceiver>();

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
		protected void elementToBeRemoved(Contact contact) {
			stopReceivingScreensFrom(contact);
		}
	};

	@SuppressWarnings("unused")
	private WatchMeReceiver _ownReceiverToAvoidGc;
	
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