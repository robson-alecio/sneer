package sneer.kernel.pointofview.impl;

import java.util.Date;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.impl.ContactIdImpl;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class FakeContact implements Contact {

	private final String _nickPrefix;
	private final Source<String> _nick;
	private FakeParty _fakeParty;

	
	public FakeContact(String nickPrefix) {
		_nickPrefix = nickPrefix;
		_nick = new SourceImpl<String>(_nickPrefix);
	}

	void randomize() {
		_nick.setter().consume(_nickPrefix + "  " + new Date());
	}

	@Override
	public ContactId id() {
		return new ContactIdImpl(0);
	}

	@Override
	public Signal<String> nick() {
		return _nick.output();
	}

	@Override
	public Party party() {
		if (_fakeParty == null)
			_fakeParty = new FakeParty(_nickPrefix);

		return _fakeParty;
	}

}
