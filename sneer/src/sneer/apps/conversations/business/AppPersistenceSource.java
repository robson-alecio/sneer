package sneer.apps.conversations.business;

import java.awt.Rectangle;

import sneer.kernel.business.contacts.ContactId;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface AppPersistenceSource {

	AppPersistence output();
	
	Omnivore<ContactId> creator();
	
	//Refactor: I need to use delegate for setters because Bubble is not a recursive proxy... :(
	public Omnivore<Pair<ContactId,MessageInfo>> archive();
	public Omnivore<Pair<ContactId,Rectangle>> boundsSetter();
	
}
