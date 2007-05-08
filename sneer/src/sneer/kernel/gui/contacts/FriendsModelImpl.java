package sneer.kernel.gui.contacts;

import sneer.kernel.business.Business;
import sneer.kernel.business.Contact;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.list.ListSignal;

public class FriendsModelImpl implements FriendsModel {

	private final Business _business;

	public FriendsModelImpl(Business business) {//refactor
		_business = business;
	}

	public void addFriend(String name) {
		throw new NotImplementedYet();
	}

	public ListSignal<Contact> friends() {
		return _business.contacts();
	}

	public void removeFriend(Contact contact) {
		throw new NotImplementedYet();
	}

}