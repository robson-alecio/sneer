package sneer.pulp.contacts.list;

import static wheel.lang.Environments.my;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import wheel.reactive.Signal;

public class ContactInfo{
	
	protected final Contact _contact;
	protected Signal<Boolean> _isOnline;

	public ContactInfo(Contact contact) {
		this(contact, my(ConnectionManager.class).connectionFor(contact).isOnline());
	}

	protected ContactInfo(Contact contact, Signal<Boolean> isOnline) {
		_contact = contact;
		_isOnline = isOnline;
	}

	public Contact contact() {
		return _contact;
	}

	public Signal<Boolean> isOnline() {
		return _isOnline;
	}
}