package sneer.kernel.business;

import java.util.List;

import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSource;

class ContactAdder implements Consumer<ContactInfo> {

	private final ListSource<Contact> _contacts;

	public ContactAdder(ListSource<Contact> contacts) {
		_contacts = contacts;
	}

	public void consume(ContactInfo info) {
		Contact contact = new ContactSource(info._nick, info._host, info._port);
		_contacts.add(contact);
	}

}
