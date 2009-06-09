package snapps.contacts.gui.comparator;

import java.util.Comparator;

import sneer.brickness.Brick;
import sneer.pulp.contacts.Contact;

@Brick
public interface ContactComparator extends Comparator<Contact> {

	int compare(Contact contact1, Contact contact2);

}