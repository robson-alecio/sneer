package sneer.contacts.impl;

import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class ContactManagerImpl implements ContactManager {
    
    private ListSource<Contact> _contacts = new ListSourceImpl<Contact>();

	@Override
	public Contact addContact(String nickame, String host, int port) {
		Contact result = new ContactImpl(nickame, host, port); 
		_contacts.add(result);
		return result;
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _contacts.output();
	}
}

class ContactImpl implements Contact {

	private String _nickname;
	
	private String _host;
	
	private int _port;
	
	
	public ContactImpl(String nickame, String host, int port) {
		_nickname = nickame;
		_host = host;
		_port = port;
	}


	@Override
	public String host() {
		return _host;
	}

	@Override
	public int port() {
		return _port;
	}


	@Override
	public String publicKey() {
		return "some public key";
	}
}