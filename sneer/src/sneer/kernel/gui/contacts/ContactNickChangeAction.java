package sneer.kernel.gui.contacts;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import wheel.lang.Omnivore;
import static wheel.i18n.Language.*;

public class ContactNickChangeAction implements ContactAction {

	public ContactNickChangeAction() {}

	public void actUpon(Contact contact) {
		
	}

	public String caption() {
		return translate("Change Nickname (not implemented yet)");
	}

}
