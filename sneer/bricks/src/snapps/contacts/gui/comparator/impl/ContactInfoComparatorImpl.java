/**
 * 
 */
package snapps.contacts.gui.comparator.impl;

import snapps.contacts.gui.comparator.ContactInfoComparator;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.list.ContactInfo;
import static wheel.lang.Environments.my;

public class ContactInfoComparatorImpl implements ContactInfoComparator {
	
	private ConnectionManager _connectionManager = my(ConnectionManager.class);

	public int compare(ContactInfo info1, ContactInfo info2) {
			boolean isOnline1 = info1.isOnline().currentValue();
			boolean isOnline2 = info2.isOnline().currentValue();

			if(isOnline1!=isOnline2){
				if(isOnline2) return 1;
				return -1;
			}
			return nick(info1).compareTo(nick(info2));
		}

	private String nick(ContactInfo info) {
		return info.contact().nickname().currentValue().toLowerCase();
	}

	Boolean isOnline(Contact contact) {
		return _connectionManager.connectionFor(contact).isOnline().currentValue();
	}
}