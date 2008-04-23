package sneer.bricks.contacts;

import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSignal;


public interface ContactManager {
    
	ListSignal<Contact> contacts();

	boolean isNicknameAlreadyUsed(String nickname);
	Contact contactGiven(String nickname);

	/** @throws IllegalParameter if there already is a Contact with that nickname.*/
	Contact addContact(String nickname) throws IllegalParameter;

	/** @throws IllegalParameter if there already is a Contact with newNickname.*/
	void changeNickname(Contact contact, String newNickname) throws IllegalParameter;
	
	

}