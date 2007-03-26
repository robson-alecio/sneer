package sneer.kernel;

import wheel.io.ui.SwingUser;

public class ContactsListing {

	public ContactsListing(SwingUser user, Domain domain) {
		String message = "Your contacts:";
		for (String contact : domain.contacts())
			message = message + "\n    " + contact;
		user.acknowledgeNotification(message);
	}

}
