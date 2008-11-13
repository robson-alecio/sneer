package sneer.pulp.contacts.list;

import sneer.kernel.container.Brick;
import sneer.pulp.contacts.Contact;
import wheel.reactive.lists.ListRegister;

//Refactor Eliminate this intermediate list. Use ContactManager.contacts() directly. ContactInfo is a non-tuple class, which is not allowed in the brick package (only interfaces and tuples are allowed). This list is serving only as a shortcut for ConnectionManager.connectionFor(contact).isOnline() - it is not worth it. 
public interface ContactList extends ListRegister<ContactInfo>, Brick{

	ContactInfo contactInfo(Contact contact);

}