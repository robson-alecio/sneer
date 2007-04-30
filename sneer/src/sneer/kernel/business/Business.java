package sneer.kernel.business;

import java.util.List;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.ListSignal;
import wheel.reactive.Signal;

public interface Business {

	Signal<String> ownName();
	Omnivore<String> ownNameSetter();

	Signal<Integer> sneerPort();
	Consumer<Integer> sneerPortSetter();

	List<Contact> contacts();
	ListSignal<Contact> contactsSignal();
	
	void addContact(String nick, String host, int port);
	void removeContact(Contact contact);

}