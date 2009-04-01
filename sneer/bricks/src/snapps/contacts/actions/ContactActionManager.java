package snapps.contacts.actions;

import java.util.Collection;

import sneer.brickness.OldBrick;

public interface ContactActionManager extends OldBrick {

	void addContactAction(ContactAction action);
	void removeContactAction(String contactActionCaption);
	Collection<ContactAction> actions();

}
