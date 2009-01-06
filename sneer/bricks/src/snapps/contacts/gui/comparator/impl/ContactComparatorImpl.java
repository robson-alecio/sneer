/**
 * 
 */
package snapps.contacts.gui.comparator.impl;

import static wheel.lang.Environments.my;
import snapps.contacts.gui.comparator.ContactComparator;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;

public class ContactComparatorImpl implements ContactComparator {
	
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);

	public int compare(Contact contact1, Contact contact2) {
		
		boolean isOnline1 = isOnline(contact1);
		boolean isOnline2 = isOnline(contact2);

			if(isOnline1 !=isOnline2){
				if(isOnline2) return 1;
				return -1;
			}
			return nick(contact1).compareTo(nick(contact2));
		}

	private String nick(Contact contact) {
		return contact.nickname().currentValue().toLowerCase();
	}

	private boolean isOnline(Contact contact) {
		return _connectionManager.connectionFor(contact).isOnline().currentValue();
	}
}