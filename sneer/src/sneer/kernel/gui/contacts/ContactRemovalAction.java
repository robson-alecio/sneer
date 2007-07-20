package sneer.kernel.gui.contacts;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import wheel.lang.Omnivore;
import static wheel.i18n.Language.*;

public class ContactRemovalAction implements ContactAction {

	private final Omnivore<ContactId> _contactRemover;

	public ContactRemovalAction(Omnivore<ContactId> contactRemover) {
		_contactRemover = contactRemover;
	}

	public void actUpon(ContactAttributes contact) {
		_contactRemover.consume(contact.id());
	}

	public String caption() {
		return translate("Delete");
	}

}
