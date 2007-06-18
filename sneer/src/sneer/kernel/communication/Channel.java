package sneer.kernel.communication;

import sneer.kernel.business.contacts.ContactId;
import wheel.io.Connection;

public interface Channel {

	Connection connectionTo(ContactId contactId);

}
