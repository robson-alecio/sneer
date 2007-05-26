package sneer.kernel.business.contacts.impl;

import java.util.List;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactSource;

import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSource;

public class ContactAdder implements Consumer<ContactInfo> {

	private final ListSource<ContactSource> _contactSources;
	private final ListSource<Contact> _contacts;

	public ContactAdder(ListSource<ContactSource> contactSources, ListSource<Contact> contacts) {
		_contactSources = contactSources;
		_contacts = contacts;
	}

	public void consume(ContactInfo info) {
		ContactSource contact = new ContactSourceImpl(info._nick, info._host, info._port);
		_contactSources.add(contact);
		_contacts.add(contact.output());
	}

}
