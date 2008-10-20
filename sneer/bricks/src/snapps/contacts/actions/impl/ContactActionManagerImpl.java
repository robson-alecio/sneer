package snapps.contacts.actions.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;

public class ContactActionManagerImpl implements ContactActionManager {

	Map<String, ContactAction> _actions = new TreeMap<String, ContactAction>();

	@Override
	public void addContactAction(ContactAction action) {
		_actions.put(action.caption(), action);
	}

	@Override
	public void removeContactAction(String contactActionCaption) {
		_actions.remove(contactActionCaption);
	}

	@Override
	public Collection<ContactAction> actions() {
		return _actions.values();
	}
}
