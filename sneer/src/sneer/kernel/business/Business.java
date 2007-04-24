package sneer.kernel.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Business implements Serializable {

	private String _ownName;

	private final List<Contact> _contactSources = new ArrayList<Contact>();
	private final List<Contact> _contactSourcesReadOnly = Collections.unmodifiableList(_contactSources);

	private int _sneerPortNumber = 0;
	
	
	public String ownName() {
		return _ownName;
	}
	
	public void ownName(String newOwnName) {
		//Implement. throw exception if empty or null;
		_ownName = newOwnName.trim();
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

	private static final long serialVersionUID = 1L;


	public boolean isOnline(Contact contact) {
		//Implement
		return false;
	}


}
