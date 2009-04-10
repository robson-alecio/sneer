package snapps.contacts.actions;

import java.util.Collection;

import sneer.brickness.OldBrick;

public interface ContactActionManager extends OldBrick {

	void addContactAction(ContactAction action);
	void addContactAction(ContactAction contactAction, boolean isDefaultAction);
	void removeContactAction(String contactActionCaption);

	ContactAction defaultAction();
	Collection<ContactAction> actions();

}
