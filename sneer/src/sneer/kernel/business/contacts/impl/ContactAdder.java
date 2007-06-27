package sneer.kernel.business.contacts.impl;

import java.util.Iterator;
import java.util.List;

import sneer.Language;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactSource;

import wheel.lang.Consumer;
import wheel.lang.Counter;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;

public class ContactAdder implements Consumer<ContactInfo> {

	private final ListSource<ContactSource> _contactSources;

	private final ListSource<Contact> _contacts;

	private final Counter _idSource;

	public ContactAdder(ListSource<ContactSource> contactSources,
			ListSource<Contact> contacts, Counter idSource) {
		_contactSources = contactSources;
		_contacts = contacts;
		_idSource = idSource;
	}

	@Override
	public void consume(ContactInfo info) throws IllegalParameter {
		checkDuplicateNickname(info);

		ContactSource contact = new ContactSourceImpl(info._nick, info._host,
				info._port, _idSource.next());
		_contactSources.add(contact);
		_contacts.add(contact.output());
	}

	private void checkDuplicateNickname(ContactInfo info)
			throws IllegalParameter {
		for (ContactSource contactSource:_contactSources) { // Optimize
			String existingNick = contactSource.output().nick().currentValue();
			if (info._nick.equals(existingNick))
				throw new IllegalParameter(String.format(Language.string("CONTACTADDER_NICKNAME_ALREADY_EXISTS"),info._nick));
		}
	}

	

}
