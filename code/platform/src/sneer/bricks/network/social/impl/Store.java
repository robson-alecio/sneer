package sneer.bricks.network.social.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.bricks.software.bricks.statestore.impl.BrickStateStoreException;

abstract class Store{

	static List<String> restore() {
		try {
			List<String> nicks = (List<String>) my(BrickStateStore.class).readObjectFor(ContactManager.class, ContactManagerImpl.class.getClassLoader());
			if(nicks!=null) 
				return nicks;
		} catch (BrickStateStoreException ignore) { } 
		return new ArrayList<String>();
	}
	
	static void save(List<Contact> currentNicks) {
		try {
			List<String> nicks = new ArrayList<String>();
			for (Contact contact : currentNicks) 
				nicks.add(contact.nickname().currentValue());

			my(BrickStateStore.class).writeObjectFor(ContactManager.class, nicks);
		} catch (BrickStateStoreException ignore) { }
	 }
}