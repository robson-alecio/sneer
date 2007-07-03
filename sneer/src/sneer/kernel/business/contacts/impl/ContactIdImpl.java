package sneer.kernel.business.contacts.impl;

import sneer.kernel.business.contacts.ContactId;
import wheel.lang.Id;

public class ContactIdImpl extends Id implements ContactId {

	public ContactIdImpl(long id) {
		super(id);
	}

	private static final long serialVersionUID = 1L;

}
