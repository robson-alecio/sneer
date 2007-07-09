package sneer.kernel.communication;

import sneer.kernel.business.contacts.ContactId;

public class Packet {

	public Packet(ContactId contactId, Object contents) {
		_contactId = contactId;
		_contents = contents;
	}
	
	public ContactId _contactId;
	public final Object _contents;
	
}
