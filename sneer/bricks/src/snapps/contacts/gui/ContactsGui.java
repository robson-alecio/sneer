package snapps.contacts.gui;

import sneer.brickness.Brick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.Signal;
import sneer.skin.main.instrumentregistry.Instrument;

@Brick
public interface ContactsGui extends Instrument {

	Signal<Contact> selectedContact();
	void clearSelection();
	
}
