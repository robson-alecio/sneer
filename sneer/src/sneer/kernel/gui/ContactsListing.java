package sneer.kernel.gui;

import sneer.kernel.business.Business;
import sneer.kernel.business.Contact;
import wheel.io.ui.User;

public class ContactsListing {

	public ContactsListing(User user, Business business) {
		StringBuffer onlineList = new StringBuffer();
		StringBuffer offlineList = new StringBuffer();
		
		for (Contact contact : business.contacts()) {
			StringBuffer list = business.isOnline(contact)
				? onlineList
				: offlineList;
			list.append("\n    " + contact);
		}
		
		if (onlineList.length() == 0) onlineList.append("\n    None");
		if (offlineList.length() == 0) offlineList.append("\n    None");

		String fullList =
			" Online Contacts:" + onlineList + "\n\n"+
			" Offline Contacts:" + offlineList;
		
		user.acknowledgeNotification(fullList);
	}

}
