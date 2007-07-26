package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.lang.Omnivore;

public class ContactRemovalAction implements ContactAction {

	private final Omnivore<ContactId> _contactRemover;

	public ContactRemovalAction(Omnivore<ContactId> contactRemover) {
		_contactRemover = contactRemover;
	}

	@Override
	public void actUpon(Contact contact) {
		_contactRemover.consume(contact.id());
	}

	@Override
	public String caption() {
		return translate("Delete");
	}

}
