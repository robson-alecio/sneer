package sneer.kernel.communication;

import sneer.kernel.business.contacts.ContactId;

public interface Operator {

	Connection connectMeWith(ContactId id);

}
