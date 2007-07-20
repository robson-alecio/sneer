package sneer.kernel.gui.contacts;

import sneer.kernel.business.contacts.ContactAttributes;

public interface ContactAction {

	String caption();
	void actUpon(ContactAttributes contact);

}
