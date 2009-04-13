package sneer.pulp.contacts;

import sneer.brickness.OldBrick;
import wheel.lang.PickyConsumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSignal;


public interface ContactManager extends OldBrick {
    
	ListSignal<Contact> contacts();

	boolean isNicknameAlreadyUsed(String nickname);
	
	Contact contactGiven(String nickname);

	/** @throws IllegalParameter if there already is a Contact with that nickname.*/
	Contact addContact(String nickname) throws IllegalParameter;

	/** Returns a contact with the given nickname. Creates a new one if there was no contact with that nickname before. */
	Contact produceContact(String nickname);

	PickyConsumer<String> nicknameSetterFor(Contact contact);
	
	void removeContact(Contact contact);


}