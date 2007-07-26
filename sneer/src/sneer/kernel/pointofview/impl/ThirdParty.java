package sneer.kernel.pointofview.impl;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public class ThirdParty implements Party {

	public ThirdParty(ContactAttributes attributes, Signal<Boolean> isOnline) {
		_attributes = attributes;
		_isOnline = isOnline;
	}

	private final ContactAttributes _attributes;
	private final Signal<Boolean> _isOnline;

	@Override
	public Signal<String> name() {
		// Implement Auto-generated method stub
		return null;
	}
	
	@Override
	public ListSignal<Contact> contacts() {
		// Implement have a thread creating and deleting random contacts, at first, just to test the gui.
		return null;
	}


	@Override
	public Signal<Boolean> publicKeyConfirmed() {
		return _attributes.publicKeyConfirmed();
	}

	@Override
	public Signal<String> host() {
		return _attributes.host();
	}

	@Override
	public Signal<Integer> port() {
		return _attributes.port();
	}

	@Override
	public Signal<Boolean> isOnline() {
		return _isOnline;
	}
	
}
