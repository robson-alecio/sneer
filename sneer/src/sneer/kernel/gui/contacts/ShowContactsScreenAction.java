package sneer.kernel.gui.contacts;

import java.util.List;
import java.util.ResourceBundle;

import static wheel.i18n.Language.*;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.lists.ListSignal;

public class ShowContactsScreenAction implements Action {

	private final ListSignal<Contact> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final Omnivore<ContactId> _contactRemover;
	private final User _user;
	private final List<ContactAction> _contactActions;
	private final Consumer<Pair<ContactId, String>> _nickChanger;

	public ShowContactsScreenAction(User user, ListSignal<Contact> contacts, List<ContactAction> contactActions, Consumer<ContactInfo> contactAdder, Omnivore<ContactId> contactRemover, Consumer<Pair<ContactId, String>> nickChanger){
		_contacts = contacts;
		_contactAdder = contactAdder;
		_user = user;
		_contactActions = contactActions;
		_contactRemover = contactRemover;
		_nickChanger = nickChanger;
	}

	public String caption() {
		return translate("Contacts");
	}

	public void run() {
		new ContactsScreen(_user, _contacts, _contactActions, _contactAdder, _contactRemover, _nickChanger); //Fix Easy: Use the existing one if there is one already.
	}

}
