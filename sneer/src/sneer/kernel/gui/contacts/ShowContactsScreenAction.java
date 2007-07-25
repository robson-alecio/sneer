package sneer.kernel.gui.contacts;

import java.awt.Frame;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import static wheel.i18n.Language.*;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import wheel.io.files.impl.DurableDirectory;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.JFrameBoundsKeeperImpl;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.lists.ListSignal;

public class ShowContactsScreenAction implements Action {

	private final ListSignal<ContactAttributes> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final Omnivore<ContactId> _contactRemover;
	private final User _user;
	private final List<ContactAction> _contactActions;
	private final Consumer<Pair<ContactId, String>> _nickChanger;
	private ContactsScreen _contactsScreen;
	private final JFrameBoundsKeeper _boundsKeeper;

	public ShowContactsScreenAction(User user, ListSignal<ContactAttributes> contacts, List<ContactAction> contactActions, Consumer<ContactInfo> contactAdder, Omnivore<ContactId> contactRemover, Consumer<Pair<ContactId, String>> nickChanger, JFrameBoundsKeeper boundsKeeper){
		_contacts = contacts;
		_contactAdder = contactAdder;
		_user = user;
		_contactActions = contactActions;
		_contactRemover = contactRemover;
		_nickChanger = nickChanger;
		_boundsKeeper = boundsKeeper;
	}

	public String caption() {
		return translate("Contacts");
	}

	public synchronized void run() {
		if (_contactsScreen == null) {
			ContactsScreen contactsScreen = new ContactsScreen(_user, _contacts, _contactActions, _contactAdder, _contactRemover, _nickChanger);
			_contactsScreen = contactsScreen;
			_boundsKeeper.keepBoundsFor(contactsScreen, ContactsScreen.class.getName());
		} 
		
		_contactsScreen.setVisible(true);
		int state = Frame.NORMAL;
		_contactsScreen.setExtendedState(state);
		_contactsScreen.toFront();
	}

}
