package sneer.kernel.gui;

import sneer.kernel.business.essence.Contact;
import sneer.kernel.business.essence.Essence;
import wheel.io.ui.User;

public class ContactsListing {

	public ContactsListing(User user, Essence essence) {
		String message = " Your contacts:";
		
		for (Contact contact : essence.contacts())
			message = message + "\n    " + contact;
		
		user.acknowledgeNotification(message);
	}

}
