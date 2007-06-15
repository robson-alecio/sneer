package sneer.kernel.gui.contacts;

import sneer.kernel.business.contacts.Contact;

public interface ContactAction {

	String caption();
	void actUpon(Contact contact);

}
