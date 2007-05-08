package sneer.kernel.gui.contacts;

import sneer.kernel.business.Contact;
import wheel.reactive.list.ListSignal;

public interface FriendsModel {

	void addFriend(String name);
	void removeFriend(Contact contact);
	
	ListSignal<Contact> friends();

}