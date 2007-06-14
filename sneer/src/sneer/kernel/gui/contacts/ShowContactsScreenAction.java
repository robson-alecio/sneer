package sneer.kernel.gui.contacts;

import java.util.List;

import sneer.apps.messages.ChatEvent;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactInfo;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.lang.Consumer;
import wheel.reactive.lists.ListSignal;

public class ShowContactsScreenAction implements Action {

	private final ListSignal<Contact> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final User _user;
	private final List<ContactAction> _contactActions;

	public ShowContactsScreenAction(ListSignal<Contact> contacts, Consumer<ContactInfo> contactAdder, User user, List<ContactAction> contactActions){
		_contacts = contacts;
		_contactAdder = contactAdder;
		_user = user;
		_contactActions = contactActions;
	}

	public String caption() {
		return "Show contacts screen";
	}

	public void run() {
		new ContactsScreen(_user, _contacts, _contactAdder, _contactActions);
	}

}
