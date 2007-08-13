package sneer.kernel.gui.contacts;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;

class ContactNode extends PartyNode {

	ContactNode(Contact contact){
		super(contact);
	}
	
	Contact contact() {
		return (Contact) getUserObject();
	}
	
	@Override
	Party party() {
		return contact().party();
	}

	private static final long serialVersionUID = 1L;
}
