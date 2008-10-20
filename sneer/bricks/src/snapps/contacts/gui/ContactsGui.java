package snapps.contacts.gui;

import sneer.skin.snappmanager.Instrument;

public interface ContactsGui extends Instrument {

	void addContactAction(ContactAction action);
	void removeContactAction(String contactActionCaption);
}
