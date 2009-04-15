package snapps.contacts.gui;

import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.Signal;
import sneer.skin.old.instrumentregistry.OldInstrument;

public interface ContactsGui extends OldInstrument {

	Signal<Contact> selectedContact();
	
}
