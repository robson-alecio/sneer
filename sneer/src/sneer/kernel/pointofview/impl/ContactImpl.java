package sneer.kernel.pointofview.impl;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.reactive.Signal;

public class ContactImpl implements Contact {

	private final ContactId _id;
	private final Signal<String> _nick;
	private final Party _party;

	public ContactImpl(ContactAttributes attributes, Signal<Boolean> isOnline) {
		_id = attributes.id();
		_nick = attributes.nick();
		_party = new ThirdParty(attributes, isOnline);
	}

	@Override
	public ContactId id() {
		return _id;
	}

	@Override
	public Signal<String> nick() {
		return _nick;
	}

	@Override
	public Party party() {
		return _party;
	}

}
