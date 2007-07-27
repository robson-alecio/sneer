package sneer.kernel.pointofview.impl;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.reactive.Signal;
import wheel.reactive.impl.ConstantSignal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class FakeParty implements Party {

	private final String _namePrefix;
	private final ListSource<Contact> _fakeContacts = createFakeContacts();

	public FakeParty(String namePrefix) {
		_namePrefix = namePrefix;
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _fakeContacts.output();
	}

	@Override
	public Signal<String> host() {
		return new ConstantSignal<String>(_namePrefix + " Name");
	}

	@Override
	public Signal<Boolean> isOnline() {
		return new ConstantSignal<Boolean>(hashCode() % 2 == 0);
	}

	@Override
	public Signal<String> name() {
		return null;
	}

	@Override
	public Signal<Integer> port() {
		return new ConstantSignal<Integer>(hashCode() % 100);
	}

	@Override
	public Signal<Boolean> publicKeyConfirmed() {
		return new ConstantSignal<Boolean>(hashCode() % 3 == 0);
	}

	private ListSource<Contact> createFakeContacts() {
		ListSourceImpl<Contact> result = new ListSourceImpl<Contact>();
		result.add(new FakeContact(_namePrefix + " 1"));
		result.add(new FakeContact(_namePrefix + " 2"));
		result.add(new FakeContact(_namePrefix + " 3"));
		return result;
	}

}
