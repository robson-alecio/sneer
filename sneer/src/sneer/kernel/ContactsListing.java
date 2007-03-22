package sneer.kernel;

import wheel.io.ui.User;

public class ContactsListing {

	public ContactsListing(User user, Domain domain) {
		String message = "Your contacts:";
		for (String contact : domain.contacts())
			message = message + "\n    " + contact;
		user.acknowledgeNotification(message);
	}

}
