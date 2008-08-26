package sneer.pulp.contacts.impl;

import java.util.Iterator;

import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class ContactManagerImpl implements ContactManager {
    
    private ListRegister<Contact> _contacts = new ListRegisterImpl<Contact>();

	@Override
	synchronized public Contact addContact(String nickname) throws IllegalParameter {
		nickname.toString();
		
		checkAvailability(nickname);
		
		Contact result = new ContactImpl(nickname); 
		_contacts.add(result);
		return result;
	}

	private void checkAvailability(String nickname) throws IllegalParameter {
		if (isNicknameAlreadyUsed(nickname))
			throw new IllegalParameter("Nickname " + nickname + " is already being used.");
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _contacts.output();
	}

	@Override
	synchronized public boolean isNicknameAlreadyUsed(String nickname) {
		return contactGiven(nickname) != null;
	}

	@Override
	synchronized public Contact contactGiven(String nickname) {
		for (Contact candidate : contacts())
			if (candidate.nickname().currentValue().equals(nickname))
				return candidate;

		return null;
	}

	synchronized public void changeNickname(Contact contact, String newNickname) throws IllegalParameter {
		checkAvailability(newNickname);
		((ContactImpl)contact).nickname(newNickname);
	}

	@Override
	public void removeContact(String nickname) throws IllegalParameter {
		Iterator<Contact> iterator = _contacts.output().iterator();
		while (iterator.hasNext()) {
			Contact contact = iterator.next();
			if(contact.nickname().currentValue().equals(nickname)){
				_contacts.remove(contact);
				return;
			}
		}
		
		throw new IllegalParameter("Nickname " + nickname + " is not used.");
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

	void nickname(String newNickname) {
		_nickname.setter().consume(newNickname);
	}

}