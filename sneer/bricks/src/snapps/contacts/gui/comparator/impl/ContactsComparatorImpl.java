/**
 * 
 */
package snapps.contacts.gui.comparator.impl;

import snapps.contacts.gui.comparator.ContactsComparator;
import sneer.kernel.container.Inject;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;

public class ContactsComparatorImpl implements ContactsComparator {
	
	@Inject
	private static ConnectionManager _connectionManager;

	public int compare(Contact contact1, Contact contact2) {
			boolean isOnline1 = isOnline(contact1);
			boolean isOnline2 = isOnline(contact2);

			if(isOnline1!=isOnline2){
				if(isOnline2) return 1;
				return -1;
			}
			return nick(contact1).compareTo(nick(contact2));
		}

	private String nick(Contact contact1) {
		return contact1.nickname().currentValue();
	}

	Boolean isOnline(Contact contact) {
		return _connectionManager.connectionFor(contact).isOnline().currentValue();
	}
}