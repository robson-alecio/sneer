package sneer.kernel.communication;

import java.io.Serializable;

import sneer.kernel.business.contacts.ContactId;

public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;

	public Packet(ContactId contactId, Object contents) {
		_contactId = contactId;
		_contents = contents;
	}
	
	public ContactId _contactId;
	public final Object _contents;
	
}
