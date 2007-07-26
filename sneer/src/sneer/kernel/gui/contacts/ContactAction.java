package sneer.kernel.gui.contacts;

import sneer.kernel.pointofview.Contact;

public interface ContactAction {

	String caption();
	void actUpon(Contact contact);

}
