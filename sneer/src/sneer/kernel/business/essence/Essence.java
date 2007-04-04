package sneer.kernel.business.essence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Essence implements Serializable {

	private String _ownName;

	private final List<Contact> _contactSources = new ArrayList<Contact>();
	private final List<Contact> _contactSourcesReadOnly = Collections.unmodifiableList(_contactSources);

	private int _sneerPortNumber = 0;
	
	
	public String ownName() {
		return _ownName;
	}
	
	public void ownName(String newOwnName) {
		int TODO_throw_exception_if_empty_or_null;
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


}
