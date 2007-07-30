package sneer.kernel.pointofview.impl;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class ThirdParty implements Party {

	public ThirdParty(ContactAttributes attributes, Signal<Boolean> isOnline) {
		
		if (attributes == null)
			throw new IllegalArgumentException();
			
		_attributes = attributes;
		_isOnline = isOnline;
		_fakeContacts = createFakeContacts();
	}

	private final ContactAttributes _attributes;
	private final Signal<Boolean> _isOnline;
	private final ListSource<Contact> _fakeContacts;

	@Override
	public Signal<String> name() {
		// Implement Auto-generated method stub
		return null;
	}
	
	private ListSource<Contact> createFakeContacts() {
		ListSourceImpl<Contact> result = new ListSourceImpl<Contact>();
		result.add(new FakeContact(nick() + " 1"));
		result.add(new FakeContact(nick() + " 2"));
		result.add(new FakeContact(nick() + " 3"));
		return result;
	}

	private String nick() {
		return _attributes.nick().currentValue();
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _fakeContacts.output();
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
