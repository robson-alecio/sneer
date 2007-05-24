package sneer.kernel.gui.contacts.tests;

import junit.framework.TestCase;
import sneer.kernel.business.Contact;
import sneer.kernel.business.ContactSource;
import sneer.kernel.business.ContactSourceImpl;
import sneer.kernel.gui.contacts.ContactPrinter;
import wheel.lang.exceptions.IllegalParameter;

public class ContactPrinterTest extends TestCase {

	public void testContactPrinter() throws Exception {
		
		ContactSource contactSource =new ContactSourceImpl("Klaus","klaus.dyndns.org", 42); 
		Contact contact = contactSource.output();
				
		ContactPrinter printer = new ContactPrinter(contact);
		Sentinel sentinel = new Sentinel(printer.output(), "Off :( - Klaus - klaus.dyndns.org:42");
		
		sentinel.expect("Off :( - KlausW - klaus.dyndns.org:42");
		contactSource.nickSetter().consume("KlausW");

		sentinel.expect("Off :( - KlausW - localhost:42");
		contactSource.hostSetter().consume("localhost");
		
		sentinel.expect("Off :( - KlausW - localhost:8080");
		contactSource.portSetter().consume(8080);
		
		sentinel.assertSatisfied();
	}
	
}
