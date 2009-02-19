package snapps.contacts.actions;

import java.util.Collection;

import sneer.kernel.container.Brick;

public interface ContactActionManager extends Brick {

	void addContactAction(ContactAction action);
	void removeContactAction(String contactActionCaption);
	Collection<ContactAction> actions();

}
