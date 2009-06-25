package sneer.bricks.snapps.contacts.actions.impl;

import java.awt.Component;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import sneer.bricks.snapps.contacts.actions.ContactAction;
import sneer.bricks.snapps.contacts.actions.ContactActionManager;

class ContactActionManagerImpl implements ContactActionManager {

	private final Map<String, ContactAction> _actions = new TreeMap<String, ContactAction>();
	private Component _baseComponent;
	
	@Override
	public Component baseComponent() {
		return _baseComponent;
	}

	@Override
	public void setBaseComponent(Component baseComponent) {
		_baseComponent = baseComponent;
	}

	private ContactAction _defaultAction = new ContactAction(){
		@Override public boolean isEnabled() { return false; }
		@Override public boolean isVisible() {return false; }
		@Override public String caption() { return "";}
		@Override public void run() {/* ignore */}
	};

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

	@Override
	public void addContactAction(ContactAction action,  boolean isDefaultAction) {
		_actions.put(action.caption(), action);
		if(isDefaultAction){
			_defaultAction = action;
		}
	}

	@Override
	public ContactAction defaultAction() {
		return _defaultAction;
	}
}
