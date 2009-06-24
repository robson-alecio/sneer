package sneer.bricks.snapps.contacts.stored.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.List;

import sneer.bricks.hardware.io.brickstatestore.BrickStateStore;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.contacts.ContactManager;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.snapps.contacts.stored.ContactStore;

class ContactStoreImpl implements ContactStore{

	private final Register<Boolean> _failToRestoreContacts = my(Signals.class).newRegister(false);
	 
	ContactStoreImpl(){
		try {
			List<Contact> contacts = (List<Contact>) my(BrickStateStore.class).readObjectFor(ContactManager.class, getClass().getClassLoader());
			ContactManager _contactManager = my(ContactManager.class);
			
			for (Contact contact : contacts) 
				_contactManager.addContact(contact.nickname().currentValue());
			
		} catch (Throwable e) {
			BlinkingLights bl = my(BlinkingLights.class);
			Light _cantRestoreContacts = bl.prepare(LightType.WARN);
			bl.turnOnIfNecessary(_cantRestoreContacts, "Unable to restore Contacts, using hardcoded Contacts", null, e);
			_failToRestoreContacts.setter().consume(true);
		} 
	 }

	 @Override
	 public void save() {
		try {
			my(BrickStateStore.class).writeObjectFor(ContactManager.class, my(ContactManager.class).contacts().currentElements());
		} catch (Exception e) {
			BlinkingLights bl = my(BlinkingLights.class);
			Light _cantStoreContacts = bl.prepare(LightType.ERROR);
			bl.turnOnIfNecessary(_cantStoreContacts, "Unable to store Contacts", null, e);
		}
	 }
	 
	 @Override
	 public Signal<Boolean> failToRestoreContacts() {
		 return _failToRestoreContacts.output();
	 }
	 
}