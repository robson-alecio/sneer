package sneer.bricks.snapps.contacts.stored.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.contacts.ContactManager;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.snapps.contacts.stored.ContactStore;
import sneer.bricks.software.bricks.statestore.BrickStateStore;

class ContactStoreImpl implements ContactStore{
	
	private final int TIMEOUT = 30*1000;
	private final Register<Boolean> _failToRestoreContacts = my(Signals.class).newRegister(false);
	private List<String> _restoredNicks;
	 
	ContactStoreImpl(){
		try {
			_restoredNicks = (List<String>) my(BrickStateStore.class).readObjectFor(ContactManager.class, getClass().getClassLoader());
		} catch (Throwable e) {
			BlinkingLights bl = my(BlinkingLights.class);
			bl.  turnOn(LightType.WARN, "Unable to restore Contacts", "Sneer can't restore your contacts, using hardcoded Contacts", e, TIMEOUT);
			_restoredNicks = new ArrayList<String>();
			_failToRestoreContacts.setter().consume(true);
		} 
	 }

	 @Override
	 public void save() {
		try {
			List<String> nicks = new ArrayList<String>();
			
			for (Contact contact : my(ContactManager.class).contacts().currentElements()) 
				nicks.add(contact.nickname().currentValue());

			my(BrickStateStore.class).writeObjectFor(ContactManager.class, nicks);
		} catch (Exception e) {
			BlinkingLights bl = my(BlinkingLights.class);
			bl.turnOn(LightType.ERROR, "Unable to store Contacts", null, e, TIMEOUT);
		}
	 }
	 
	 @Override
	 public Signal<Boolean> failToRestoreContacts() {
		 return _failToRestoreContacts.output();
	 }
	 
	 @Override
	 public List<String> getRestoredNicks(){
		 return _restoredNicks;
	 }
}