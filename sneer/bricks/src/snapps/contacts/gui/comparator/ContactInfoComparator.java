package snapps.contacts.gui.comparator;

import java.util.Comparator;

import sneer.pulp.contacts.list.ContactInfo;

public interface ContactInfoComparator extends Comparator<ContactInfo>{

	int compare(ContactInfo info1, ContactInfo info2);

}