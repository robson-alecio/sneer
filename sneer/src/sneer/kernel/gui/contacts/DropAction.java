package sneer.kernel.gui.contacts;

import sneer.kernel.pointofview.Contact;

public interface DropAction {
	String caption();
	boolean interested(Object object);
	void actUpon(Contact contact, Object object);
}
