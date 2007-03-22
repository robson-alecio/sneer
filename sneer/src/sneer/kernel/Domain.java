package sneer.kernel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Domain implements Serializable {

	private String _ownName;

	private final List<String> _contacts = new ArrayList<String>();

	
	public String ownName() {
		return _ownName;
	}
	
	public void ownName(String newOwnName) {
		int TODO_throw_exception_if_empty_or_null;
		_ownName = newOwnName.trim();
	}
	
	public List<String> contacts() {
		return _contacts;
	}

	public void addContact(String nick) {
		_contacts.add(nick);
	}

	private static final long serialVersionUID = 1L;


}
