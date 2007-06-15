package sneer.kernel.gui.contacts.tests;

import junit.framework.TestCase;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactSource;
import sneer.kernel.business.contacts.impl.ContactSourceImpl;
import sneer.kernel.gui.contacts.ContactListPrinter;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class ContactListPrinterTest extends TestCase {

	public void testContactListPrinter() throws Exception {
		ListSource<Contact> contacts = new ListSourceImpl<Contact>();

		ContactSource klaus = new ContactSourceImpl("Klaus","klaus.dyndns.org", 42, 0);
		ContactSource kalecser = new ContactSourceImpl("Kalecser","kalecser.dyndns.org", 42, 0);
		contacts.add(klaus.output()); 
		contacts.add(kalecser.output());

		ContactListPrinter subject = new ContactListPrinter(contacts.output());
			
		ListPrinter listPrinter = new ListPrinter(subject.output());
		Sentinel sentinel = new Sentinel(listPrinter.output(), 
				"Off :( - Klaus - klaus.dyndns.org:42," +
				"Off :( - Kalecser - kalecser.dyndns.org:42");
		
		sentinel.expect(
				"Off :( - KW - klaus.dyndns.org:42," +
				"Off :( - Kalecser - kalecser.dyndns.org:42");
		klaus.nickSetter().consume("KW");

		sentinel.expect(
				"Off :( - KW - klaus.dyndns.org:42," +
				"Off :( - Kalecser - kalecser.dyndns.org:43");
		kalecser.portSetter().consume(43);

		sentinel.expect(
				"Off :( - KW - klaus.dyndns.org:42," +
				"Off :( - Kalecser - localhost:43");
		kalecser.hostSetter().consume("localhost");

		sentinel.expect(
				"On  :) - KW - klaus.dyndns.org:42," +
				"Off :( - Kalecser - localhost:43");
		klaus.isOnlineSetter().consume(true);
		
		sentinel.expect(
				"Off :( - Kalecser - localhost:43");
		contacts.remove(klaus.output());

		ContactSource humberto = new ContactSourceImpl("Humba","humba.selfip.org", 8080, 0);
		sentinel.expect(
		"Off :( - Kalecser - localhost:43," +
		"Off :( - Humba - humba.selfip.org:8080");
		contacts.add(humberto.output());
		
		//Fix: test contacts.replace(...);
		
		sentinel.assertSatisfied();
	}
	
}
