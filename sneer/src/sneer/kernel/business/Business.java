package sneer.kernel.business;

import sneer.apps.messages.ChatEvent;
import sneer.kernel.business.contacts.Contact;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface Business {

	Signal<String> ownName();
	
	Signal<Integer> sneerPort();

	ListSignal<Contact> contacts();

}
