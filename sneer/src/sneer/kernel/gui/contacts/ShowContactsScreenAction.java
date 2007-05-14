package sneer.kernel.gui.contacts;

import sneer.kernel.business.Contact;
import sneer.kernel.business.ContactInfo;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.lang.Consumer;
import wheel.reactive.list.ListSignal;

public class ShowContactsScreenAction implements Action {

	private final ListSignal<Contact> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final User _user;

	public ShowContactsScreenAction(ListSignal<Contact> contacts, Consumer<ContactInfo> contactAdder, User user){
		_contacts = contacts;
		_contactAdder = contactAdder;
		_user = user;
	}

	public String caption() {
		return "Show contacts screen";
	}

	public void run() {
		new FriendsScreen(_contacts, _contactAdder, _user);
	}

}
