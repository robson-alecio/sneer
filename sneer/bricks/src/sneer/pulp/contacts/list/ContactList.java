package sneer.pulp.contacts.list;

import sneer.kernel.container.Brick;
import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListRegister;

public interface ContactList extends ListRegister<ContactInfo>, Brick{

	ContactInfo contactInfo(Contact contact);

}