package sneer.bricks.pulp.contacts.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.io.brickstatestore.BrickStateStore;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.contacts.ContactManager;

class Store{
	
	private final int TIMEOUT = 30*1000;
	private List<String> _nicks;
	
	Store(){
		try {
			_nicks  = (List<String>) my(BrickStateStore.class).readObjectFor(ContactManager.class, getClass().getClassLoader());
		} catch (Throwable e) {
			initializeOnError(e);
		} 
	 }

	private void initializeOnError(Throwable e) {
		BlinkingLights bl = my(BlinkingLights.class);
		bl.  turnOn(LightType.WARN, "Unable to restore Contacts", "Sneer can't restore your contacts, using hardcoded Contacts", e, TIMEOUT);
		_nicks = new ArrayList<String>();
	}

	void save() {
		try {
			List<String> nicks = new ArrayList<String>();
			for (Contact contact : my(ContactManager.class).contacts().currentElements()) 
				nicks.add(contact.nickname().currentValue());

			my(BrickStateStore.class).writeObjectFor(ContactManager.class, nicks);
			_nicks = nicks;
		} catch (Exception e) {
			BlinkingLights bl = my(BlinkingLights.class);
			bl.turnOn(LightType.ERROR, "Unable to store Contacts", null, e, TIMEOUT);
		}
	 }

	List<String> getRestoredNicks() {
		return _nicks;
	}
}