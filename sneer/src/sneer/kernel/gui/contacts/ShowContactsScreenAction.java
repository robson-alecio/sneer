package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Frame;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.Action;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.User;

public class ShowContactsScreenAction implements Action {

	private final Party _I;
	private final User _user;
	private ContactsScreen _contactsScreen;
	private final JFrameBoundsKeeper _boundsKeeper;
	private final ContactActionFactory _contactActionFactory;
	private final BusinessSource _businnessSource;

	public ShowContactsScreenAction(User user, Party I, ContactActionFactory contactActionFactory, BusinessSource businnessSource, JFrameBoundsKeeper boundsKeeper){
		_I = I;
		_contactActionFactory = contactActionFactory;
		_businnessSource = businnessSource;
		_user = user;
		_boundsKeeper = boundsKeeper;
	}

	public String caption() {
		return translate("Contacts");
	}

	public synchronized void run() {
		if (_contactsScreen == null) {
			_contactsScreen = new ContactsScreen(_user, _I, _contactActionFactory, _businnessSource);
			_boundsKeeper.keepBoundsFor(_contactsScreen, ContactsScreen.class.getName());
		} 
		
		_contactsScreen.setVisible(true);
		int state = Frame.NORMAL;
		_contactsScreen.setExtendedState(state);
		_contactsScreen.toFront();
	}

}
