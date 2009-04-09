package snapps.contacts.actions;

import sneer.pulp.contacts.Contact;
import wheel.io.ui.action.Action;

public interface ContactAction extends Action{

	//Refactor: remove setActive (see ContactsGui.selectedContact )
	void setActive(Contact contact);
	boolean isVisible();
	boolean isEnabled();
}