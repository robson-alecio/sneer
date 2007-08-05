package sneer.kernel.pointofview.tests;

import sneer.kernel.pointofview.Contact;
import wheel.lang.Omnivore;

interface PartySimulator {

	Omnivore<String> nameSetter();
	Contact contact(String nick);

}
