package snapps.contacts.gui;

import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.Signal;
import sneer.skin.snappmanager.OldInstrument;

public interface ContactsGui extends OldInstrument {

	Signal<Contact> selectedContact();
	
}
