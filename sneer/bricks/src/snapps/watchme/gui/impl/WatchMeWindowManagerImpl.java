package snapps.watchme.gui.impl;

import java.util.HashMap;
import java.util.Map;

import snapps.watchme.gui.WatchMeWindowManager;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;

public class WatchMeWindowManagerImpl implements WatchMeWindowManager{

	@Inject
	private static KeyManager _keyManager;
	
	Map<Contact, WatchMeWindow> _windows = new HashMap<Contact, WatchMeWindow>();
	
	@Override
	public void createWatchMeWindowFor(Contact contact) {
		if(_windows.containsKey(contact)) return;
		
		PublicKey key = _keyManager.keyGiven(contact);
		
//		//Fix Klaus, how works keymanager?
//		if(key==null) {
//			key = _keyManager.generateMickeyMouseKey(contact.nickname().currentValue());
//			_keyManager.addKey(contact, key);
//		}
		
		WatchMeWindow window = new WatchMeWindow(key);
		_windows.put(contact, window);
	}

	@Override
	public void disposeWindow(Contact contact) {
		WatchMeWindow window = _windows.get(contact);
		if(window==null) return;
		_windows.remove(contact);
		window.dispose();
	}
}
