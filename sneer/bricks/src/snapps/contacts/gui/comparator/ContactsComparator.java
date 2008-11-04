package snapps.contacts.gui.comparator;

import java.util.Comparator;

import sneer.pulp.contacts.Contact;

public interface ContactsComparator extends Comparator<Contact>{

	int compare(Contact contact1, Contact contact2);

}