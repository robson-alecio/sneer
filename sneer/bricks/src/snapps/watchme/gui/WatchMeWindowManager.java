package snapps.watchme.gui;

import sneer.pulp.contacts.Contact;

public interface WatchMeWindowManager {

	void createWatchMeWindowFor(Contact contact);
	void disposeWindow(Contact contact);
}
