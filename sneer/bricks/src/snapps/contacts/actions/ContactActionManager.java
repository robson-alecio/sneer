package snapps.contacts.actions;

import java.util.Collection;

public interface ContactActionManager {

	void addContactAction(ContactAction action);
	void removeContactAction(String contactActionCaption);
	Collection<ContactAction> actions();

}
