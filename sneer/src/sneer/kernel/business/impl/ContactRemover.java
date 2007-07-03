package sneer.kernel.business.impl;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactSource;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSource;

public class ContactRemover implements Omnivore<ContactId> {

	private final ListSource<ContactSource> _contactSources;
	private final ListSource<Contact> _contacts;

	public ContactRemover(ListSource<ContactSource> contactSources, ListSource<Contact> contacts) {
		_contactSources = contactSources;
		_contacts = contacts;
	}

	public void consume(ContactId contactId) {
		for(ContactSource contactSource:_contactSources.output()){
			if (contactSource.output().id().equals(contactId)){
				_contactSources.remove(contactSource);
				_contacts.remove(contactSource.output());
				break;
			}
		}
	}

}
