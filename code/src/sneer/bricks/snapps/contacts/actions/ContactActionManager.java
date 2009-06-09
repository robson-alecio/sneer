package sneer.bricks.snapps.contacts.actions;

import java.util.Collection;

import sneer.foundation.brickness.Brick;

@Brick
public interface ContactActionManager {

	void addContactAction(ContactAction action);
	void addContactAction(ContactAction contactAction, boolean isDefaultAction);
	void removeContactAction(String contactActionCaption);

	ContactAction defaultAction();
	Collection<ContactAction> actions();

}
