package sneer.kernel.pointofview.impl;

import sneer.kernel.business.Business;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.graphics.JpgImage;
import wheel.reactive.Signal;
import wheel.reactive.impl.ConstantSignal;
import wheel.reactive.lists.ListSignal;

class Me implements Party {

	Me(Business business, ListSignal<Contact> contacts) {
		_business = business;
		_contacts = contacts;
	}
	
	private final Business _business;
	private final ListSignal<Contact> _contacts;

	@Override
	public ListSignal<Contact> contacts() {
		return _contacts;
	}

	@Override
	public Signal<String> name() {
		return _business.ownName();
	}

	@Override
	public Signal<JpgImage> picture() {
		// Implement Auto-generated method stub
		return null;
	}

	@Override
	public Signal<String> profile() {
		// Implement Auto-generated method stub
		return null;
	}

	@Override
	public Signal<String> thoughtOfTheDay() {
		// Implement Auto-generated method stub
		return null;
	}

	@Override
	public Signal<Boolean> identityConfirmed() {
		return new ConstantSignal<Boolean>(true);
	}

	@Override
	public Signal<Boolean> isOnline() {
		// Implement Auto-generated method stub
		return null;
	}

}
