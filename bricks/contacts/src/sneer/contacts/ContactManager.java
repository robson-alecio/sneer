package sneer.contacts;

import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSignal;


public interface ContactManager {
    
	/** @throws IllegalParameter if there already is a Contact with that nickname.*/
	Contact addContact(String nickname) throws IllegalParameter;
	
	boolean isNicknameAlreadyUsed(String nickname);
	
	ListSignal<Contact> contacts();
	
	Contact contactGiven(String nickname);
}