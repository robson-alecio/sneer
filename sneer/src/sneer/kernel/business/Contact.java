package sneer.kernel.business;

import sneer.kernel.gui.contacts.ContactListPrinter;
import wheel.reactive.Signal;

public interface Contact {

	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();

	Signal<Boolean> isOnline();
	
}
