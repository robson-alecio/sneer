package sneer.kernel.business;

import java.util.List;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.ListSignal;
import wheel.reactive.Signal;

public interface Business {

	Signal<String> ownName();
	
	Signal<Integer> sneerPort();

	ListSignal<Contact> contactsSignal();
	List<Contact> contacts();

//----------------------------------
	
	Omnivore<String> ownNameSetter();
	
	Consumer<Integer> sneerPortSetter();

	void addContact(String nick, String host, int port);
	void removeContact(Contact contact);
}