package snapps.contacts.gui.comparator;

import java.util.Comparator;

import sneer.brickness.Brick;
import sneer.pulp.contacts.Contact;

public interface ContactComparator extends Comparator<Contact>, Brick {

	int compare(Contact contact1, Contact contact2);

}