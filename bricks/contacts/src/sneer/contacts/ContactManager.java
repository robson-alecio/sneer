package sneer.contacts;

import wheel.reactive.lists.ListSignal;


public interface ContactManager {
    
	Contact addContact(String nickame, String host, int port);
	
	ListSignal<Contact> contacts();
}