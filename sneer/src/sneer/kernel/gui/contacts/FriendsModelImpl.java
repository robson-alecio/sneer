package sneer.kernel.gui.contacts;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.Contact;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.list.ListSignal;

public class FriendsModelImpl implements FriendsModel {

	private final BusinessSource _business;

	public FriendsModelImpl(BusinessSource business) {//refactor
		_business = business;
	}

	public void addFriend(String name) {
		throw new NotImplementedYet();
	}

	public ListSignal<Contact> friends() {
		return _business.output().contacts();
	}

	public void removeFriend(Contact contact) {
		throw new NotImplementedYet();
	}

}