package sneer.kernel.gui.contacts;

import javax.swing.Icon;

import sneer.kernel.business.contacts.Contact;

public interface ContactAction {

	String caption();
	void actUpon(Contact contact);

}
