package sneer.bricks.network.social.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;

class ContactManagerImpl implements ContactManager {
    
    private final ListRegister<Contact> _contacts = my(CollectionSignals.class).newListRegister();
    
    ContactManagerImpl(){
		restore();
    }

	private void restore() {
			for (String nick : Store.restore())
				if (!isWeird(nick)) //Filter out those weird nicknames that appeared in the beginning.
					produceContact(nick);
	}
    
	private boolean isWeird(String nick) {
		return nick.length() > 100;
	}

	
	@Override
	synchronized public Contact addContact(String nickname) throws Refusal {
		nickname.toString();
		
		checkAvailability(nickname);
		
		return doAddContact(nickname);
	}

	private void save() {
		Store.save(_contacts.output().currentElements());
	}

	private Contact doAddContact(String nickname) {
		Contact result = new ContactImpl(nickname); 
		_contacts.add(result);
		save();
		return result;
	}

	private void checkAvailability(String nickname) throws Refusal {
		if (isNicknameAlreadyUsed(nickname))
			throw new Refusal("Nickname " + nickname + " is already being used.");
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

	synchronized private void changeNickname(Contact contact, String newNickname) throws Refusal {
		checkAvailability(newNickname);
		((ContactImpl)contact).nickname(newNickname);
		save();
	}

	@Override
	public void removeContact(Contact contact) {
		_contacts.remove(contact);
		save();
	}
	
	@Override
	public PickyConsumer<String> nicknameSetterFor(final Contact contact) {
		return new PickyConsumer<String>(){ @Override public void consume(String newNickname) throws Refusal {
			changeNickname(contact, newNickname);
		}};
	}

	@Override
	public synchronized Contact produceContact(String nickname) {
		if (contactGiven(nickname) == null) doAddContact(nickname);
		return contactGiven(nickname);
	}
}