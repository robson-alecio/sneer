package sneer.kernel.business;

import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Business {

	Signal<String> ownName();
	Omnivore<String> ownNameSetter();

	int sneerPortNumber();

	List<Contact> contacts();
	void addContact(String nick, String host, int port);

}