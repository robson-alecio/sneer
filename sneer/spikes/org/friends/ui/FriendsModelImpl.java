package org.friends.ui;

import sneer.kernel.business.Business;
import sneer.kernel.business.Contact;
import wheel.reactive.ListSignal;

public class FriendsModelImpl implements FriendsModel {

	private final Business _business;

	public FriendsModelImpl(Business business) {
		_business = business;
	}

	public void addFriend(String name) {
		_business.addContact(name, "", 0);
	}

	public ListSignal<Contact> friends() {
		return _business.contactsSignal();
	}

	public void removeFriend(Contact contact) {
		_business.removeContact(contact);
	}

}