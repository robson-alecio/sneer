package snapps.contacts.gui.comparator;

import java.util.Comparator;

import sneer.brickness.OldBrick;
import sneer.pulp.contacts.Contact;

public interface ContactComparator extends Comparator<Contact>, OldBrick {

	int compare(Contact contact1, Contact contact2);

}