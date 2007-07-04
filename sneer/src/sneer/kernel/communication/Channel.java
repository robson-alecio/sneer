package sneer.kernel.communication;

import sneer.kernel.business.contacts.ContactId;
import wheel.io.Connection;
import wheel.reactive.Signal;

public interface Channel {

	Connection connectionTo(ContactId contactId);
	Signal<ContactId> lastContactRequestingConnection();
	
}
