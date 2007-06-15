package sneer.kernel.communication;

import sneer.kernel.business.contacts.ContactId;

public interface Channel {

	Connection connectionTo(ContactId contactId);

}
