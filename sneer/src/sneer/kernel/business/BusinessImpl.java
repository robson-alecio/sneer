package sneer.kernel.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.SourceImpl;


public class BusinessImpl implements Serializable, Business {

	private SourceImpl<String> _ownName = new SourceImpl<String>("");

	private final List<Contact> _contactSources = new ArrayList<Contact>();
	private final List<Contact> _contactSourcesReadOnly = Collections.unmodifiableList(_contactSources);

	private int _sneerPortNumber = 0;
	
	
	public Signal<String> ownName() {
		return _ownName.output();
	}
	
	public Omnivore<String> ownNameSetter() {
		return _ownName.setter();
	}

	public int sneerPortNumber() {
		return _sneerPortNumber;
	}
	
	public List<Contact> contacts() {
		return _contactSourcesReadOnly;
	}

	public void addContact(String nick, String host, int port) {
		_contactSources.add(new ContactSource(nick, host, port));
	}

	public boolean isOnline(Contact contact) {
		//Implement
		return false;
	}


	private static final long serialVersionUID = 1L;
}
