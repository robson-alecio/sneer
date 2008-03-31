package sneer.contacts.impl;

import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class ContactManagerImpl implements ContactManager {
    
    private ListSource<Contact> _contacts = new ListSourceImpl<Contact>();

	@Override
	public Contact addContact(String nickname) throws IllegalParameter {
		nickname.toString();
		
		if (isNicknameAlreadyUsed(nickname))
			throw new IllegalParameter("Nickname " + nickname + " is already being used.");
		
		Contact result = new ContactImpl(nickname); 
		_contacts.add(result);
		return result;
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _contacts.output();
	}

	@Override
	public boolean isNicknameAlreadyUsed(String nickname) {
		for (Contact contact : _contacts.output())
			if (contact.nickname().equals(nickname)) return true;
		
		return false;
	}
}

class ContactImpl implements Contact {

	private final Register<String> _nickname;
	
	
	public ContactImpl(String nickname) {
		_nickname = new RegisterImpl<String>(nickname);
	}


	@Override
	public Signal<String> nickname() {
		return _nickname.output();
	}


	@Override
	public String toString() {
		return _nickname.output().currentValue();
	}

}