package snapps.contacts.gui;

import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.Signal;
import sneer.skin.main.instrumentregistry.Instrument;

public interface ContactsGui extends Instrument {

	Signal<Contact> selectedContact();
	
}
