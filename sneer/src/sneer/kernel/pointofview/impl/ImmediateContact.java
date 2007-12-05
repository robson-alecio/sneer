package sneer.kernel.pointofview.impl;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.lang.Pair;
import wheel.reactive.Signal;

public class ImmediateContact implements Contact {

	private final ContactId _id;
	private final Signal<String> _nick;
	private final Party _party;

	public ImmediateContact(ContactAttributes attributes, Signal<Boolean> isOnline, Channel channel, Signal<Pair<String, Boolean>> isOnlineOnMsnEvents) {
		_id = attributes.id();
		_nick = attributes.nick();
		_party = new ThirdParty(attributes, isOnline, channel, isOnlineOnMsnEvents);
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
	
	@Override
	public String toString(){
		return _nick.currentValue();
	}

	private static final long serialVersionUID = 1L;
}
