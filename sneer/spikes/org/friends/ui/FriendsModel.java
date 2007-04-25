package org.friends.ui;

import sneer.kernel.business.Contact;
import wheel.reactive.ListSignal;

public interface FriendsModel {

	void addFriend(String name);
	void removeFriend(Contact contact);
	
	ListSignal<Contact> friends();

}