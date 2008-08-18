package sneer.pulp.contacts;

import sneer.lego.Brick;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSignal;


public interface ContactManager extends Brick {
    
	ListSignal<Contact> contacts();

	boolean isNicknameAlreadyUsed(String nickname);
	Contact contactGiven(String nickname);

	/** @throws IllegalParameter if there already is a Contact with that nickname.*/
	Contact addContact(String nickname) throws IllegalParameter;

	/** @throws IllegalParameter if there already is a Contact with newNickname.*/
	void changeNickname(Contact contact, String newNickname) throws IllegalParameter;
	
	

}