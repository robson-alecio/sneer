package snapps.watchme.gui.impl;

import java.util.HashMap;
import java.util.Map;

import snapps.watchme.WatchMe;
import snapps.watchme.gui.WatchMeWindowManager;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.own.name.OwnNameKeeper;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class WatchMeWindowManagerImpl implements WatchMeWindowManager{

	@Inject
	private static KeyManager _keyManager;
	
	@Inject
	static private ContactManager _contactManager;
	
	@Inject
	private static OwnNameKeeper _ownName;
	
	@Inject
	private static WatchMe _watchMe;
	
	Map<Contact, WatchMeWindow> _windows = new HashMap<Contact, WatchMeWindow>();

	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _receiverToAvoidGc = new SimpleListReceiver<Contact>(_contactManager.contacts()){

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
	
	public WatchMeWindowManagerImpl() {
		WatchMeWindow watchMeWindow = new WatchMeWindow(_keyManager.ownPublicKey());
		watchMeWindow.setVisible(true);
	}

	public void startReceivingScreensFrom(Contact contact) {
		if (contact.nickname().equals(ownName())) return;		
		
		if(_windows.containsKey(contact)) return;
		
		PublicKey key = _keyManager.keyGiven(contact);
			
		WatchMeWindow window = new WatchMeWindow(key);
		_windows.put(contact, window);
	}

	public void stopReceivingScreensFrom(Contact contact) {
		WatchMeWindow window = _windows.get(contact);
		if(window==null) return;
		_windows.remove(contact);
		window.dispose();
	}

	private String ownName() {
		return _ownName.name().currentValue();
	}
}
