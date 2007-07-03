package sneer.kernel.gui.contacts;

import java.util.List;
import java.util.ResourceBundle;

import static sneer.Language.*;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

public class ShowContactsScreenAction implements Action {

	private final ListSignal<Contact> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final Omnivore<ContactId> _contactRemover;
	private final User _user;
	private final List<ContactAction> _contactActions;

	public ShowContactsScreenAction(ListSignal<Contact> contacts, Consumer<ContactInfo> contactAdder, Omnivore<ContactId> contactRemover, User user, List<ContactAction> contactActions){
		_contacts = contacts;
		_contactAdder = contactAdder;
		_user = user;
		_contactActions = contactActions;
		_contactRemover = contactRemover;
	}

	public String caption() {
		return translate("Contacts");
	}

	public void run() {
		new ContactsScreen(_user, _contacts, _contactAdder, _contactRemover, _contactActions);
	}

}
