package sneer.kernel.pointofview.impl;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.impl.ContactIdImpl;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.reactive.Signal;
import wheel.reactive.impl.ConstantSignal;

public class FakeContact implements Contact {

	private final String _nickPrefix;

	public FakeContact(String nickPrefix) {
		_nickPrefix = nickPrefix;
	}

	@Override
	public ContactId id() {
		return new ContactIdImpl(0);
	}

	@Override
	public Signal<String> nick() {
		return new ConstantSignal<String>(_nickPrefix);
	}

	@Override
	public Party party() {
		return new FakeParty(_nickPrefix);
	}

}
