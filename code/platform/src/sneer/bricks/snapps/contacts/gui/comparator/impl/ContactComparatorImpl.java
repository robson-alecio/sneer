/**
 * 
 */
package sneer.bricks.snapps.contacts.gui.comparator.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.heartbeat.stethoscope.Stethoscope;
import sneer.bricks.snapps.contacts.gui.comparator.ContactComparator;

class ContactComparatorImpl implements ContactComparator {
	
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
		return my(Stethoscope.class).isAlive(contact).currentValue();
	}
}