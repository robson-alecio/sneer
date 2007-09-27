package sneer.apps.asker.packet;

import java.io.Serializable;

import sneer.kernel.business.contacts.ContactId;

public abstract class AskerRequestPayload implements Serializable{
	public ContactId _contactId; //filled later by asker (security)
	
	public abstract String prompt();

}
