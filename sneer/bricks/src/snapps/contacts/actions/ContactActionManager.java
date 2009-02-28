package snapps.contacts.actions;

import java.util.Collection;

import sneer.brickness.Brick;

public interface ContactActionManager extends Brick {

	void addContactAction(ContactAction action);
	void removeContactAction(String contactActionCaption);
	Collection<ContactAction> actions();

}
