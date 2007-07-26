package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Frame;
import java.util.List;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo2;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

public class ShowContactsScreenAction implements Action {

	private final Party _I;
	private final Consumer<ContactInfo2> _contactAdder;
	private final Omnivore<ContactId> _contactRemover;
	private final User _user;
	private final List<ContactAction> _contactActions;
	private final Consumer<Pair<ContactId, String>> _nickChanger;
	private ContactsScreen _contactsScreen;
	private final JFrameBoundsKeeper _boundsKeeper;

	public ShowContactsScreenAction(User user, Party I, List<ContactAction> contactActions, Consumer<ContactInfo2> contactAdder, Omnivore<ContactId> contactRemover, Consumer<Pair<ContactId, String>> nickChanger, JFrameBoundsKeeper boundsKeeper){
		_I = I;
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
			_contactsScreen = new ContactsScreen(_user, _I, _contactActions, _contactAdder, _contactRemover, _nickChanger);
			_boundsKeeper.keepBoundsFor(_contactsScreen, ContactsScreen.class.getName());
		} 
		
		_contactsScreen.setVisible(true);
		int state = Frame.NORMAL;
		_contactsScreen.setExtendedState(state);
		_contactsScreen.toFront();
	}

}
