package sneer.bricks.snapps.contacts.gui.comparator;

import java.util.Comparator;

import sneer.bricks.pulp.contacts.Contact;
import sneer.foundation.brickness.Brick;

@Brick
public interface ContactComparator extends Comparator<Contact> {

	int compare(Contact contact1, Contact contact2);

}