package sneer.apps.conversations.business;

import sneer.kernel.business.contacts.ContactId;

public interface AppPersistence {
	
	ConversationsPersistenceSource persistenceFor(ContactId contactId);
	
}
