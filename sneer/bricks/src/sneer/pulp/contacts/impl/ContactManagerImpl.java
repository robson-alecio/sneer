package sneer.pulp.contacts.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.software.exceptions.IllegalParameter;
import sneer.software.lang.PickyConsumer;

class ContactManagerImpl implements ContactManager {
    
    private final ListRegister<Contact> _contacts = my(ReactiveCollections.class).newListRegister();

	@Override
	synchronized public Contact addContact(String nickname) throws IllegalParameter {
		nickname.toString();
		
		checkAvailability(nickname);
		
		return doAddContact(nickname);
	}

	private Contact doAddContact(String nickname) {
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

	synchronized private void changeNickname(Contact contact, String newNickname) throws IllegalParameter {
		checkAvailability(newNickname);
		((ContactImpl)contact).nickname(newNickname);
	}

	@Override
	public void removeContact(Contact contact) {
		_contacts.remove(contact);
	}
	

	@Override
	public PickyConsumer<String> nicknameSetterFor(final Contact contact) {
		return new PickyConsumer<String>(){ @Override public void consume(String newNickname) throws IllegalParameter {
			changeNickname(contact, newNickname);
		}};
	}

	@Override
	public Contact produceContact(String nickname) {
		if (contactGiven(nickname) == null) doAddContact(nickname);
		return contactGiven(nickname);
	}
}

class ContactImpl implements Contact {

	private final Register<String> _nickname;
	
	public ContactImpl(String nickname) {
		_nickname = my(Signals.class).newRegister(nickname);
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